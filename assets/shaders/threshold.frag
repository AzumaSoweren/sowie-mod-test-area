uniform lowp sampler2D u_texture;

uniform lowp float threshold;

const lowp vec3 luminanceCoeff = vec3(0.2126, 0.7152, 0.0722);

varying vec2 v_texCoords;

void main() {
    vec4 color = texture2D(u_texture, v_texCoords);

    float luminance = dot(color.rgb, luminanceCoeff);

    if (luminance > threshold) gl_FragColor = color;
    else gl_FragColor = vec4(0.0);
}