package com.beanbeanjuice.meme.api.wrapper.objects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RedditMeme {

    private final String title;
    private final String subreddit;
    private final String url;
    private final String imageUrl;
    private final boolean isNsfw;
    private final String author;

}
