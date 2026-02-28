attribute vec4 a_position;
attribute vec2 a_texCoord0;

uniform vec2 u_offset;
uniform vec2 u_size;
uniform float u_scale;

varying vec2 v_texCoord0;
varying vec2 v_texCoord1;
varying vec2 v_texCoord2;
varying vec2 v_texCoord3;
varying vec2 v_texCoord4;

void main() {
    vec2 offset = u_offset / u_size * u_scale;

    v_texCoord0 = a_texCoord0;
    v_texCoord1 = a_texCoord0 + vec2(-offset.x, offset.y);
    v_texCoord2 = a_texCoord0 + offset;
    v_texCoord3 = a_texCoord0 - offset;
    v_texCoord4 = a_texCoord0 - vec2(-offset.x, offset.y);

    gl_Position = a_position;
}