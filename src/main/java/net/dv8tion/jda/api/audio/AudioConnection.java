package net.dv8tion.jda.api.audio;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents an audio connection.
 */
public class AudioConnection
{
    private final JDA jda;
    private final long guildId;

    private Long channelId;
    private boolean selfMuted;
    private boolean selfDeafened;

    public AudioConnection(@Nonnull JDA jda, long guildId, @Nullable Long channelId, boolean selfMuted, boolean selfDeafened)
    {
        this.jda = jda;
        this.guildId = guildId;
        this.channelId = channelId;
        this.selfMuted = selfMuted;
        this.selfDeafened = selfDeafened;
    }

    /**
     * The guild that this audio connection in.
     * @return The guild's id
     */
    public long getGuildId()
    {
        return guildId;
    }

    /**
     * The channel that we're connected to.
     * @return The channel's id
     */
    public long getChannelId()
    {
        return channelId;
    }

    /**
     *
     * @param channelId ID of the channel to connect to.
     */
    public void setChannelId(long channelId)
    {
        this.channelId = channelId;
    }

    @Nullable
    public VoiceChannel getChannel()
    {
        return jda.getVoiceChannelById(channelId);
    }

    public JDA getJda()
    {
        return jda;
    }

    public boolean isSelfDeafened()
    {
        return selfDeafened;
    }

    public void setSelfDeafened(boolean selfDeafened)
    {
        this.selfDeafened = selfDeafened;
        updateVoiceState();
    }

    public boolean isSelfMuted()
    {
        return selfMuted;
    }

    public void setSelfMuted(boolean selfMuted)
    {
        this.selfMuted = selfMuted;
        updateVoiceState();
    }

    @Nullable
    public Guild getGuild()
    {
        return jda.getGuildById(guildId);
    }

    public void updateVoiceState()
    {
        VoiceChannel channel = getChannel();
        if (channel != null)
        {
            //This is technically equivalent to an audio open/move packet.
            getJda().getDirectAudioController().connect(channel, selfMuted, selfDeafened);
        }
    }
}
