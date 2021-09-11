/*
 * Copyright 2015 Austin Keener, Michael Ritter, Florian Spieß, and the JDA contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.dv8tion.jda.internal.managers;

import gnu.trove.map.TLongObjectMap;
import net.dv8tion.jda.api.audio.AudioConnection;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.DirectAudioController;
import net.dv8tion.jda.api.utils.cache.CacheView;
import net.dv8tion.jda.internal.JDAImpl;
import net.dv8tion.jda.internal.requests.WebSocketClient;
import net.dv8tion.jda.internal.utils.Checks;
import net.dv8tion.jda.internal.utils.UnlockHook;
import net.dv8tion.jda.internal.utils.cache.AbstractCacheView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class DirectAudioControllerImpl implements DirectAudioController
{
    private final JDAImpl api;
    private final AbstractCacheView<AudioConnection> audioConnections = new CacheView.SimpleCacheView<>(AudioConnection.class, m -> m.getGuild().getName());

    public DirectAudioControllerImpl(JDAImpl api)
    {
        this.api = api;
    }

    @Override
    public @NotNull AbstractCacheView<AudioConnection> getAudioConnectionCache()
    {
        return audioConnections;
    }

    @Nullable
    @Override
    public AudioConnection getAudioConnection(long guildId)
    {
        return null;
    }

    public void setAudioConnection(long guildId, AudioConnection connection) {
        AbstractCacheView<AudioConnection> managerView = getJDA().getDirectAudioController().getAudioConnectionCache();
        try (UnlockHook hook = managerView.writeLock())
        {
            TLongObjectMap<AudioConnection> managerMap = managerView.getMap();
            managerMap.put(guildId, connection);
            hook.close();
        }
    }

    @Nonnull
    @Override
    public JDAImpl getJDA()
    {
        return api;
    }

    @Override
    public void connect(@Nonnull VoiceChannel channel, boolean selfMute, boolean selfDeaf)
    {
        Checks.notNull(channel, "Voice Channel");
        JDAImpl jda = getJDA();
        WebSocketClient client = jda.getClient();
        client.queueAudioConnect(channel, selfMute, selfDeaf);
    }

    @Override
    public void disconnect(@Nonnull Guild guild)
    {
        Checks.notNull(guild, "Guild");
        JDAImpl jda = getJDA();
        WebSocketClient client = jda.getClient();
        client.queueAudioDisconnect(guild);
    }

    @Override
    public void reconnect(@Nonnull VoiceChannel channel)
    {
        Checks.notNull(channel, "Voice Channel");
        JDAImpl jda = getJDA();
        WebSocketClient client = jda.getClient();
        client.queueAudioReconnect(channel);
    }

    /**
     * Used to update the internal state of the voice request. When a connection
     * was successfully established JDA will stop sending requests for the initial connect.
     * <br>This is done to retry the voice updates in case of a partial service failure.
     *
     * <p>Should be called when:
     * <ol>
     *     <li>Receiving a Voice State Update for the current account and we were previously connected (moved or disconnected)</li>
     *     <li>Receiving a Voice Server Update (initial connect or region change)</li>
     * </ol>
     *
     * Note that the voice state update will always be received prior to a voice server update.
     * <br>The internal dispatch handlers already call this when needed, a library end-user never needs to call this method.
     *
     * @param guild
     *        The guild to update the state for
     * @param channel
     *        The new channel, or null to signal disconnect
     */
    public void update(Guild guild, VoiceChannel channel)
    {
        Checks.notNull(guild, "Guild");
        JDAImpl jda = getJDA();
        WebSocketClient client = jda.getClient();
        client.updateAudioConnection(guild.getIdLong(), channel);
    }
}
