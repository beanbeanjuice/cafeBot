package com.beanbeanjuice.utility.music.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * A class used for managing the audio to be sent.
 */
public class AudioPlayerSendHandler implements AudioSendHandler {

    private final AudioPlayer audioPlayer;
    private final ByteBuffer buffer;
    private final MutableAudioFrame frame;

    /**
     * Creates a new instance of the {@link AudioPlayerSendHandler} object.
     * @param audioPlayer The {@link AudioPlayer} to be used.
     */
    public AudioPlayerSendHandler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.buffer = ByteBuffer.allocate(1024);
        this.frame = new MutableAudioFrame();
        this.frame.setBuffer(buffer);
    }

    /**
     * I'm gonna be honest. I have no idea what this is.
     * @return Whether or not the {@link AudioPlayer} can provide.
     */
    @Override
    public boolean canProvide() {
        return this.audioPlayer.provide(this.frame);
    }

    /**
     * @return The {@link ByteBuffer} used for transmitting audio.
     */
    @Override
    public ByteBuffer provide20MsAudio() {
        final Buffer tmp = ((Buffer) this.buffer).flip();
        return (ByteBuffer) tmp;
    }

    /**
     * @return Whether or not to use OpusVoice.
     */
    @Override
    public boolean isOpus() {
        return true;
    }

}
