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

package net.dv8tion.jda.api.managers;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.audio.AudioConnection;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.utils.cache.CacheView;
import net.dv8tion.jda.internal.utils.cache.AbstractCacheView;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Direct access to internal gateway communication.
 * <br>This should only be used if a {@link net.dv8tion.jda.api.hooks.VoiceDispatchInterceptor VoiceDispatchInterceptor} has been provided.
 */
public interface DirectAudioController
{

    /**
     * {@link net.dv8tion.jda.api.utils.cache.CacheView CacheView} of
     * all cached {@link net.dv8tion.jda.api.audio.AudioConnection} created for this JDA instance.
     *
     * <p>AudioManagers are cross-session persistent!
     *
     * @return {@link net.dv8tion.jda.api.utils.cache.CacheView CacheView}
     */
    @Nonnull
    AbstractCacheView<AudioConnection> getAudioConnectionCache();

    /**
     * Immutable list of all created {@link net.dv8tion.jda.api.audio.AudioConnection AudioConnections} for this JDA instance!
     *
     * @return Immutable list of all created AudioManager instances
     */
    @Nonnull
    default List<AudioConnection> getAudioManagers()
    {
        return getAudioConnectionCache().asList();
    }

    /**
     *
     * @param guildId
     * @return
     */
    @Nullable
    AudioConnection getAudioConnection(long guildId);

    /**
     * The associated JDA instance
     *
     * @return The JDA instance
     */
    @Nonnull
    JDA getJDA();

    /**
     * Requests a voice server endpoint for connecting to the voice gateway.
     *
     * @param channel
     *        The channel to connect to
     * @param selfDeaf
     *        Whether the bot will be deafened.
     * @param selfMute
     *        Whether the bot will be muted.
     *
     * @see   #reconnect(VoiceChannel)
     */
    void connect(@Nonnull VoiceChannel channel, boolean selfMute, boolean selfDeaf);

    /**
     * Requests to terminate the connection to a voice channel.
     *
     * @param guild
     *        The guild we were connected to
     *
     * @see   #reconnect(VoiceChannel)
     */
    void disconnect(@Nonnull Guild guild);

    /**
     * Requests to reconnect to the voice channel in the target guild.
     *
     * @param channel
     *        The channel we were connected to
     */
    void reconnect(@Nonnull VoiceChannel channel);
}
