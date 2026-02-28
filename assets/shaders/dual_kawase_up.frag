uniform lowp sampler2D u_texture;

varying vec2 v_texCoord1;
varying vec2 v_texCoord2;
varying vec2 v_texCoord3;
varying vec2 v_texCoord4;
varying vec2 v_texCoord5;
varying vec2 v_texCoord6;
varying vec2 v_texCoord7;
varying vec2 v_texCoord8;

void main() {
    vec4 color = vec4(0.0);
    color += texture2D(u_texture, v_texCoord1);
    color += texture2D(u_texture, v_texCoord2);
    color += texture2D(u_texture, v_texCoord3);
    color += texture2D(u_texture, v_texCoord4);
    color *= 2.0;

    color += texture2D(u_texture, v_texCoord5);
    color += texture2D(u_texture, v_texCoord6);
    color += texture2D(u_texture, v_texCoord7);
    color += texture2D(u_texture, v_texCoord8);

    gl_FragColor = color / 12.0;
}