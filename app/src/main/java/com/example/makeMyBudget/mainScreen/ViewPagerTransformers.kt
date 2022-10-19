package com.example.makeMyBudget.mainScreen

import android.view.View
import androidx.viewpager2.widget.ViewPager2.PageTransformer
import kotlin.math.abs

sealed class ViewPagerTransformers {
    class ForegroundToBackgroundTransformer : PageTransformer {
        override fun transformPage(page: View, position: Float) {
            val pageWidth = page.width
            page.apply {
                when {
                    position < -1 -> { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        alpha = 0f
                    }
                    position <= 0 -> { // [-1,0]
                        // Use the default slide transition when moving to the left page
                        alpha = 1f
                        translationX = 0f
                        translationZ = 0f
                        scaleX = 1f
                        scaleY = 1f
                    }
                    position <= 1 -> { // (0,1]
                        // Fade the page out.
                        alpha = 1 - position
                        // Counteract the default slide transition
                        translationX = pageWidth * -position
                        // Move it behind the left page
                        translationZ = -1f
                        // Scale the page down (between MIN_SCALE and 1)
                        val scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - abs(position))
                        scaleX = scaleFactor
                        scaleY = scaleFactor
                    }
                    else -> { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        alpha = 0f
                    }
                }
            }
        }

        companion object {
            private const val MIN_SCALE = 0.75f
        }

    }

    class DepthPageTransformer : PageTransformer {
        override fun transformPage(page: View, position: Float) {
            val pageWidth = page.width
            page.apply {
                when {
                    position < -1 -> { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        alpha = 0f
                    }
                    position <= 0 -> { // [-1,0]
                        // Use the default slide transition when moving to the left page
                        alpha = 1f
                        translationX = 0f
                        translationZ = 0f
                        scaleX = 1f
                        scaleY = 1f
                    }
                    position <= 1 -> { // (0,1]
                        // Fade the page out.
                        alpha = 1 - position
                        // Counteract the default slide transition
                        translationX = pageWidth * -position
                        // Move it behind the left page
                        translationZ = -1f
                        // Scale the page down (between MIN_SCALE and 1)
                        val scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - abs(position))
                        scaleX = scaleFactor
                        scaleY = scaleFactor
                    }
                    else -> { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        alpha = 0f
                    }
                }
            }
        }

        companion object {
            private const val MIN_SCALE = 0.75f
        }

    }

    class TabletPageTransformer : PageTransformer {

        override fun transformPage(page: View, position: Float) {
            val pageWidth = page.width
            page.apply {
                when {
                    position < -1 -> { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        alpha = 0f
                    }
                    position <= 0 -> { // [-1,0]
                        // Use the default slide transition when moving to the left page
                        alpha = 1f
                        translationX = 0f
                        translationZ = 0f
                        scaleX = 1f
                        scaleY = 1f
                    }
                    position <= 1 -> { // (0,1]
                        // Fade the page out.
                        alpha = 1 - position
                        // Counteract the default slide transition
                        translationX = pageWidth * -position
                        // Move it behind the left page
                        translationZ = -1f
                        // Scale the page down (between MIN_SCALE and 1)
                        val scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - abs(position))
                        scaleX = scaleFactor
                        scaleY = scaleFactor
                    }
                    else -> { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        alpha = 0f
                    }
                }
            }
        }

        companion object {
            private const val MIN_SCALE = 0.75f
        }

    }

    class ZoomOutPageTransformer : PageTransformer {
        override fun transformPage(page: View, position: Float) {
            val pageWidth = page.width
            val pageHeight = page.height
            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                page.alpha = 0f
            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                val scaleFactor = MIN_SCALE.coerceAtLeast(1 - abs(position))
                val vertMargin = pageHeight * (1 - scaleFactor) / 2
                val horzMargin = pageWidth * (1 - scaleFactor) / 2
                if (position < 0) {
                    page.translationX = horzMargin - vertMargin / 2
                } else {
                    page.translationX = -horzMargin + vertMargin / 2
                }
                // Scale the page down (between MIN_SCALE and 1)
                page.scaleX = scaleFactor
                page.scaleY = scaleFactor
                // Fade the page relative to its size.
                page.alpha =
                    MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA)
            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                page.alpha = 0f
            }
        }

        companion object {
            private const val MIN_SCALE = 0.85f
            private const val MIN_ALPHA = 0.5f
        }
    }
}
