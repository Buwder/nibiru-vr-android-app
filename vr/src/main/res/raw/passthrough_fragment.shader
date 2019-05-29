precision mediump float;
varying vec4 v_Color;

void main() {
    gl_FragColor = v_Color;
    gl_FragColor.w = 1.0;
}
