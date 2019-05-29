precision mediump float;
varying vec4 v_Color;
varying vec3 v_Grid;

void main() {
//	int a = 100;int b=10;
//	for(int i=0;i<a;i++){ for(int j=0;j<b;j++){b=b+a;}}
			
    float depth = gl_FragCoord.z / gl_FragCoord.w; // Calculate world-space distance.

    if ((mod(abs(v_Grid.x), 10.0) < 0.1) || (mod(abs(v_Grid.z), 10.0) < 0.1)) {
        gl_FragColor = max(0.0, (90.0-depth) / 90.0) * vec4(1.0, 1.0, 1.0, 1.0)
                + min(1.0, depth / 90.0) * v_Color;
    } else {
        gl_FragColor = v_Color;
    }
    gl_FragColor.w = 1.0;
}
