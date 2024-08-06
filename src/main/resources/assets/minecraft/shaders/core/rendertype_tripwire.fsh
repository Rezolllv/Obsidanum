#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;
uniform float GameTime;

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
in vec4 normal;
in vec3 pos;

out vec4 fragColor;

void main() {

    vec4 color = texture(Sampler0, texCoord0);
    vec2 texSize = textureSize(Sampler0, 0);
    int testCol = int(texelFetch(Sampler0, ivec2(texCoord0 * texSize), 0).a * 255);

    if (testCol != 247)
	{
    color *= vertexColor * ColorModulator;
	}

    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
