#!/bin/sh

mkdir opengl_specs

echo "####  Downloading function specifications"
wget http://www.opengl.org/registry/api/gl.spec -O opengl_specs/gl.spec

echo "####  Downloading enumeration specifications"
wget http://www.opengl.org/registry/api/enum.spec -O opengl_specs/enum.spec

echo "####  Checking out docbook format documentation for opengl"
svn co --username anonymous --password anonymous https://cvs.khronos.org/svn/repos/ogl/trunk/ecosystem/public/sdk/docs/man/ opengl_specs/docs



