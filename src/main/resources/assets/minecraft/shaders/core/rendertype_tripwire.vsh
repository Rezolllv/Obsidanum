#version 150

#moj_import <fog.glsl>
#moj_import <light.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV2;
in vec3 Normal;

uniform sampler2D Sampler0;
uniform sampler2D Sampler2;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

uniform vec3 ChunkOffset;

uniform vec4 FogColor;
uniform int FogShape;

out float vertexDistance;
out vec4 vertexColor;
out vec2 texCoord0;
out vec4 normal;
out vec3 pos;

void main() {
    vec3 Pos = Position + ChunkOffset;

    gl_Position = ProjMat * ModelViewMat * vec4(Pos, 1.0);

    vertexDistance = fog_distance(ModelViewMat, Pos, FogShape);
    vertexColor = Color * minecraft_sample_lightmap(Sampler2, UV2);
    texCoord0 = UV0;
    pos = Pos;
    normal = ProjMat * ModelViewMat * vec4(Normal, 0.0);
}