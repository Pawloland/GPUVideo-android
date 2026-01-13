package com.daasuu.gpuv.player;

public enum PlayerScaleType {
    RESIZE_FIT,  // Follows aspect ratio of the video and fits video within view, so no parts are cut off thus black bars may be visible
    RESIZE_FILL, // Doesn't follow aspect ratio of the video and stretches video's width and height to match view's width and height so no black bars are visible and no parts are cut off
    RESIZE_ZOOM, // Follows aspect ratio of the video and zooms in so that no black bars are visible but parts of the video may be cut off
}
