uniform lowp sampler2D u_texture;

varying vec2 v_texCoord0;
varying vec2 v_texCoord1;
varying vec2 v_texCoord2;
varying vec2 v_texCoord3;
varying vec2 v_texCoord4;

void main() {
    vec4 color = texture2D(u_texture, v_texCoord0) * 4.0;
    color += texture2D(u_texture, v_texCoord1);
    color += texture2D(u_texture, v_texCoord2);
    color += texture2D(u_texture, v_texCoord3);
    color += texture2D(u_texture, v_texCoord4);

    gl_FragColor = color / 8.0;
}