package testarea.y2026.m02.shader.graphics.g2d

import arc.graphics.Color
import arc.graphics.Gl
import arc.graphics.gl.FrameBuffer
import arc.graphics.gl.Shader
import arc.math.Mathf
import mindustry.Vars
import mindustry.graphics.Shaders
import testarea.SorcTest

//TODO
class ShaderTestBloom {
    companion object {
        private val root = Vars.mods.getMod(SorcTest::class.java).root.child("shaders")
        private const val DOWN_NAME = "dual_kawase_down"
        private const val UP_NAME = "dual_kawase_up"

        const val MAX_ITERATIONS = 8
    }

    private val filter = Shader(root.child("screenspace.vert"), root.child("threshold.frag"))
    private val bloom = Shader(root.child("screenspace.vert"), root.child("bloom.frag"))
    private val down = Shader(root.child("$DOWN_NAME.vert"), root.child("$DOWN_NAME.frag"))
    private val up = Shader(root.child("$UP_NAME.vert"), root.child("$UP_NAME.frag"))

    private var current = MAX_ITERATIONS
//    private var iterations = MAX_ITERATIONS
    private var threshold = 0f
    private var offset = 0f
    private var bloomIntensity = 0f
    private var originalIntensity = 0f
    private var scale = 0f

    private var capturing = false
    private var clearColor = Color.clear

    private lateinit var capturer: FrameBuffer
    private lateinit var buffers: List<FrameBuffer>

    init {
        rebuild()
        setIterations(3)
        setThreshold(0.5f)
        setOffset(0.2f)
        setBloomIntensity(2.5f)
        setOriginalIntensity(1f)

        bloom.bind()
        bloom.setUniformi("u_texture1", 1)
    }

    private fun rebuild() {
        capturer = FrameBuffer(2, 2, false)
        buffers = List(MAX_ITERATIONS + 1) { FrameBuffer(2, 2, false) }
    }

    fun resize(width: Int, height: Int) {
        if (capturer.width == width && capturer.height == height) return

        capturer.resize(width, height)
        for (i in 0..current) buffers[i].resize(width shr i, height shr i)
    }

    fun begin() {
        if (capturing) return
        capturing = true
        capturer.begin()
        Gl.clearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a)
        Gl.clear(Gl.colorBufferBit or Gl.depthBufferBit)
    }

    fun end() {
        if (capturing) {
            capturing = false
            capturer.end()
        }
    }

    fun render() {
        end()

        Gl.disable(Gl.blend)
        Gl.disable(Gl.depthTest)
        Gl.depthMask(false)

        capture(0) {
            capturer.blit(filter)
        }

        down.bind()
        for (level in 1..current) {
            down.setUniformf("u_size", buffers[level - 1].width.toFloat(), buffers[level - 1].height.toFloat())
            capture(level) {
                blit(level - 1, down)
            }
        }

        up.bind()
        for (level in current downTo 1) {
            up.setUniformf("u_size", buffers[level].width.toFloat(), buffers[level].height.toFloat())
            capture(level - 1) {
                blit(level, up)
            }
        }

        Gl.enable(Gl.blend)
        Gl.blendFunc(Gl.srcAlpha, Gl.oneMinusSrcAlpha)

        buffers[0].texture.bind(1)
        capturer.blit(bloom)
    }
    
    fun setIterations(target: Int) {
        current = target.coerceAtMost(MAX_ITERATIONS)
    }

    fun setThreshold(value: Float) {
        val value = Mathf.clamp(value)
        if (threshold == value) return
        threshold = value

        filter.bind()
        filter.setUniformf("u_threshold", value)
    }
    
    fun setBloomIntensity(intensity: Float) {
        if (bloomIntensity == intensity) return
        bloomIntensity = intensity

        bloom.bind()
        bloom.setUniformf("bloomIntensity", intensity)
    }

    fun setOriginalIntensity(intensity: Float) {
        if (originalIntensity == intensity) return
        originalIntensity = intensity

        bloom.bind()
        bloom.setUniformf("originalIntensity", intensity)
    }

    fun setOffset(offset: Float) {
        if (this.offset == offset) return
        this.offset = offset

        down.bind()
        down.setUniformf("u_offset", offset, offset)
        up.bind()
        up.setUniformf("u_offset", offset, offset)
    }

    fun setScale(scale: Float) {
//        val scale = scale.coerceAtLeast(2f)
        if (this.scale == scale) return
        this.scale = scale

        down.bind()
        down.setUniformf("u_scale", scale)
        up.bind()
        up.setUniformf("u_scale", scale)
    }

    private fun blit(level: Int, shader: Shader = Shaders.screenspace) = buffers[level].blit(shader)

    private fun capture(level: Int, drawer: () -> Unit) {
        buffers[level].begin()
        drawer()
        buffers[level].end()
    }

    fun dispose() {
        try {
            for (buffer in buffers) buffer.dispose()

            down.dispose()
            up.dispose()
        } catch (_: Throwable) {
        }
    }
}