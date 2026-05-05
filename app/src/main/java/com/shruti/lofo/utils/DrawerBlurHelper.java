package com.shruti.lofo.utils;

import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import android.graphics.RenderEffect;
import android.graphics.Shader;

public class DrawerBlurHelper {

    /// Maximum blur radius for RenderEffect (API 31+)
    private static final float MAX_BLUR_RADIUS = 18f;

    private final View contentView;
    private final View overlayView;

    public DrawerBlurHelper(@NonNull View contentView, @NonNull View overlayView) {
        this.contentView = contentView;
        this.overlayView = overlayView;
    }

    /**
     * Call this from onDrawerSlide.
     * slideOffset ranges from 0f (closed) to 1f (fully open).
     */
    public void onDrawerSlide(float slideOffset) {
        applyOverlayDim(slideOffset);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            applyRenderEffectBlur(slideOffset);
        }
        // Below API 31: dim overlay alone gives a clean feel
    }

    /**
     * Call this from onDrawerClosed to fully reset state.
     */
    public void onDrawerClosed() {
        overlayView.setAlpha(0f);
        overlayView.setClickable(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            contentView.setRenderEffect(null);
        }
    }

    /**
     * Call this from onDrawerOpened so overlay blocks touches on content.
     */
    public void onDrawerOpened() {
        overlayView.setAlpha(1f);
        overlayView.setClickable(true);
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private void applyOverlayDim(float slideOffset) {
        overlayView.setAlpha(slideOffset * 0.85f);
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void applyRenderEffectBlur(float slideOffset) {
        float radius = slideOffset * MAX_BLUR_RADIUS;

        if (radius < 0.1f) {
            contentView.setRenderEffect(null);
            return;
        }

        contentView.setRenderEffect(
                RenderEffect.createBlurEffect(radius, radius, Shader.TileMode.CLAMP)
        );
    }
}