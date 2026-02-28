attribute vec4 a_position;
attribute vec2 a_texCoord0;

uniform vec2 u_offset;
uniform vec2 u_size;
uniform float u_scale;

varying vec2 v_texCoord1;
varying vec2 v_texCoord2;
varying vec2 v_texCoord3;
varying vec2 v_texCoord4;

varying vec2 v_texCoord5;
varying vec2 v_texCoord6;
varying vec2 v_texCoord7;
varying vec2 v_texCoord8;

void main() {
    vec2 offset = u_offset / u_size * u_scale;

    v_texCoord1 = a_texCoord0 + vec2(-offset.x, offset.y);
    v_texCoord2 = a_texCoord0 + offset;
    v_texCoord3 = a_texCoord0 - offset;
    v_texCoord4 = a_texCoord0 - vec2(-offset.x, offset.y);
    v_texCoord5 = a_texCoord0 + vec2(offset.x * 2.0, 0.0);
    v_texCoord6 = a_texCoord0 - vec2(offset.x * 2.0, 0.0);
    v_texCoord7 = a_texCoord0 + vec2(0.0, offset.x * 2.0);
    v_texCoord8 = a_texCoord0 - vec2(0.0, offset.x * 2.0);

    gl_Position = a_position;
}