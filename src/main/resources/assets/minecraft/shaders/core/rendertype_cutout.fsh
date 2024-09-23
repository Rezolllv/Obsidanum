#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

in float vertexDistance;
in vec4 vertexColor;
in vec2 texCoord0;
in vec4 normal;

out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler0, texCoord0);
    vec2 texSize = textureSize(Sampler0, 0);
    int testCol = int(texelFetch(Sampler0, ivec2(texCoord0 * texSize), 0).a * 255);

    // Предполагаем, что свечение активируется для пикселей с определенным значением в альфа-канале
    if (testCol != 247) { // Значение 247 взято по аналогии с rendertype_translucent.fsh
        color *= vertexColor * ColorModulator; // Активируем эффект свечения
    }

    // Отсекаем прозрачные пиксели
    if (color.a < 0.1) {
        discard;
    }

    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}