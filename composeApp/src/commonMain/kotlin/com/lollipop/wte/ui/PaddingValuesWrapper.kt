package com.lollipop.wte.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import com.lollipop.wte.ui.PaddingValuesWrapper.Edge

class PaddingValuesWrapper(
    private val basePadding: PaddingValues,
    private val startEdge: Edge = Edge.Enable,
    private val topEdge: Edge = Edge.Enable,
    private val endEdge: Edge = Edge.Enable,
    private val bottomEdge: Edge = Edge.Enable,
) : PaddingValues {
    override fun calculateBottomPadding(): Dp {
        return bottomEdge.parse(basePadding.calculateBottomPadding())
    }

    override fun calculateLeftPadding(layoutDirection: LayoutDirection): Dp {
        return startEdge.parse(basePadding.calculateLeftPadding(layoutDirection))
    }

    override fun calculateRightPadding(layoutDirection: LayoutDirection): Dp {
        return endEdge.parse(basePadding.calculateRightPadding(layoutDirection))
    }

    override fun calculateTopPadding(): Dp {
        return topEdge.parse(basePadding.calculateTopPadding())
    }

    sealed class Edge {

        abstract fun parse(src: Dp): Dp

        data object Disable : Edge() {
            override fun parse(src: Dp): Dp {
                return 0.dp
            }
        }

        data object Enable : Edge() {
            override fun parse(src: Dp): Dp {
                return src
            }
        }

        class Min(val minValue: Dp) : Edge() {
            override fun parse(src: Dp): Dp {
                return min(minValue, src)
            }

        }

        class Max(val maxValue: Dp) : Edge() {
            override fun parse(src: Dp): Dp {
                return max(maxValue, src)
            }
        }
    }
}

fun PaddingValues.wrapperOf(
    startEdge: Edge = Edge.Enable,
    topEdge: Edge = Edge.Enable,
    endEdge: Edge = Edge.Enable,
    bottomEdge: Edge = Edge.Enable,
): PaddingValuesWrapper {
    return PaddingValuesWrapper(this, startEdge, topEdge, endEdge, bottomEdge)
}
