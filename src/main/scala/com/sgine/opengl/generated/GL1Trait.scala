package com.sgine.opengl.generated

import com.sgine.opengl.GLContext
import javax.media.opengl._
import javax.media.opengl.fixedfunc._;

trait GL1Trait {
	val GL_DEPTH_BUFFER_BIT = GL.GL_DEPTH_BUFFER_BIT;
	val GL_STENCIL_BUFFER_BIT = GL.GL_STENCIL_BUFFER_BIT;
	val GL_COLOR_BUFFER_BIT = GL.GL_COLOR_BUFFER_BIT;
	val GL_FALSE = GL.GL_FALSE;
	val GL_TRUE = GL.GL_TRUE;
	val GL_NONE = GL.GL_NONE;
	val GL_POINTS = GL.GL_POINTS;
	val GL_LINES = GL.GL_LINES;
	val GL_LINE_LOOP = GL.GL_LINE_LOOP;
	val GL_LINE_STRIP = GL.GL_LINE_STRIP;
	val GL_TRIANGLES = GL.GL_TRIANGLES;
	val GL_TRIANGLE_STRIP = GL.GL_TRIANGLE_STRIP;
	val GL_TRIANGLE_FAN = GL.GL_TRIANGLE_FAN;
	val GL_ZERO = GL.GL_ZERO;
	val GL_ONE = GL.GL_ONE;
	val GL_SRC_COLOR = GL.GL_SRC_COLOR;
	val GL_ONE_MINUS_SRC_COLOR = GL.GL_ONE_MINUS_SRC_COLOR;
	val GL_SRC_ALPHA = GL.GL_SRC_ALPHA;
	val GL_ONE_MINUS_SRC_ALPHA = GL.GL_ONE_MINUS_SRC_ALPHA;
	val GL_DST_ALPHA = GL.GL_DST_ALPHA;
	val GL_ONE_MINUS_DST_ALPHA = GL.GL_ONE_MINUS_DST_ALPHA;
	val GL_DST_COLOR = GL.GL_DST_COLOR;
	val GL_ONE_MINUS_DST_COLOR = GL.GL_ONE_MINUS_DST_COLOR;
	val GL_SRC_ALPHA_SATURATE = GL.GL_SRC_ALPHA_SATURATE;
	val GL_FUNC_ADD = GL.GL_FUNC_ADD;
	val GL_BLEND_EQUATION = GL.GL_BLEND_EQUATION;
	val GL_BLEND_EQUATION_RGB = GL.GL_BLEND_EQUATION_RGB;
	val GL_BLEND_EQUATION_ALPHA = GL.GL_BLEND_EQUATION_ALPHA;
	val GL_FUNC_SUBTRACT = GL.GL_FUNC_SUBTRACT;
	val GL_FUNC_REVERSE_SUBTRACT = GL.GL_FUNC_REVERSE_SUBTRACT;
	val GL_BLEND_DST_RGB = GL.GL_BLEND_DST_RGB;
	val GL_BLEND_SRC_RGB = GL.GL_BLEND_SRC_RGB;
	val GL_BLEND_DST_ALPHA = GL.GL_BLEND_DST_ALPHA;
	val GL_BLEND_SRC_ALPHA = GL.GL_BLEND_SRC_ALPHA;
	val GL_ARRAY_BUFFER = GL.GL_ARRAY_BUFFER;
	val GL_ELEMENT_ARRAY_BUFFER = GL.GL_ELEMENT_ARRAY_BUFFER;
	val GL_ARRAY_BUFFER_BINDING = GL.GL_ARRAY_BUFFER_BINDING;
	val GL_ELEMENT_ARRAY_BUFFER_BINDING = GL.GL_ELEMENT_ARRAY_BUFFER_BINDING;
	val GL_STATIC_DRAW = GL.GL_STATIC_DRAW;
	val GL_DYNAMIC_DRAW = GL.GL_DYNAMIC_DRAW;
	val GL_BUFFER_SIZE = GL.GL_BUFFER_SIZE;
	val GL_BUFFER_USAGE = GL.GL_BUFFER_USAGE;
	val GL_FRONT = GL.GL_FRONT;
	val GL_BACK = GL.GL_BACK;
	val GL_FRONT_AND_BACK = GL.GL_FRONT_AND_BACK;
	val GL_TEXTURE_2D = GL.GL_TEXTURE_2D;
	val GL_CULL_FACE = GL.GL_CULL_FACE;
	val GL_BLEND = GL.GL_BLEND;
	val GL_DITHER = GL.GL_DITHER;
	val GL_STENCIL_TEST = GL.GL_STENCIL_TEST;
	val GL_DEPTH_TEST = GL.GL_DEPTH_TEST;
	val GL_SCISSOR_TEST = GL.GL_SCISSOR_TEST;
	val GL_POLYGON_OFFSET_FILL = GL.GL_POLYGON_OFFSET_FILL;
	val GL_SAMPLE_ALPHA_TO_COVERAGE = GL.GL_SAMPLE_ALPHA_TO_COVERAGE;
	val GL_SAMPLE_COVERAGE = GL.GL_SAMPLE_COVERAGE;
	val GL_NO_ERROR = GL.GL_NO_ERROR;
	val GL_INVALID_ENUM = GL.GL_INVALID_ENUM;
	val GL_INVALID_VALUE = GL.GL_INVALID_VALUE;
	val GL_INVALID_OPERATION = GL.GL_INVALID_OPERATION;
	val GL_OUT_OF_MEMORY = GL.GL_OUT_OF_MEMORY;
	val GL_CW = GL.GL_CW;
	val GL_CCW = GL.GL_CCW;
	val GL_LINE_WIDTH = GL.GL_LINE_WIDTH;
	val GL_ALIASED_POINT_SIZE_RANGE = GL.GL_ALIASED_POINT_SIZE_RANGE;
	val GL_ALIASED_LINE_WIDTH_RANGE = GL.GL_ALIASED_LINE_WIDTH_RANGE;
	val GL_CULL_FACE_MODE = GL.GL_CULL_FACE_MODE;
	val GL_FRONT_FACE = GL.GL_FRONT_FACE;
	val GL_DEPTH_RANGE = GL.GL_DEPTH_RANGE;
	val GL_DEPTH_WRITEMASK = GL.GL_DEPTH_WRITEMASK;
	val GL_DEPTH_CLEAR_VALUE = GL.GL_DEPTH_CLEAR_VALUE;
	val GL_DEPTH_FUNC = GL.GL_DEPTH_FUNC;
	val GL_STENCIL_CLEAR_VALUE = GL.GL_STENCIL_CLEAR_VALUE;
	val GL_STENCIL_FUNC = GL.GL_STENCIL_FUNC;
	val GL_STENCIL_FAIL = GL.GL_STENCIL_FAIL;
	val GL_STENCIL_PASS_DEPTH_FAIL = GL.GL_STENCIL_PASS_DEPTH_FAIL;
	val GL_STENCIL_PASS_DEPTH_PASS = GL.GL_STENCIL_PASS_DEPTH_PASS;
	val GL_STENCIL_REF = GL.GL_STENCIL_REF;
	val GL_STENCIL_VALUE_MASK = GL.GL_STENCIL_VALUE_MASK;
	val GL_STENCIL_WRITEMASK = GL.GL_STENCIL_WRITEMASK;
	val GL_VIEWPORT = GL.GL_VIEWPORT;
	val GL_SCISSOR_BOX = GL.GL_SCISSOR_BOX;
	val GL_COLOR_CLEAR_VALUE = GL.GL_COLOR_CLEAR_VALUE;
	val GL_COLOR_WRITEMASK = GL.GL_COLOR_WRITEMASK;
	val GL_UNPACK_ALIGNMENT = GL.GL_UNPACK_ALIGNMENT;
	val GL_PACK_ALIGNMENT = GL.GL_PACK_ALIGNMENT;
	val GL_MAX_TEXTURE_SIZE = GL.GL_MAX_TEXTURE_SIZE;
	val GL_MAX_VIEWPORT_DIMS = GL.GL_MAX_VIEWPORT_DIMS;
	val GL_SUBPIXEL_BITS = GL.GL_SUBPIXEL_BITS;
	val GL_RED_BITS = GL.GL_RED_BITS;
	val GL_GREEN_BITS = GL.GL_GREEN_BITS;
	val GL_BLUE_BITS = GL.GL_BLUE_BITS;
	val GL_ALPHA_BITS = GL.GL_ALPHA_BITS;
	val GL_DEPTH_BITS = GL.GL_DEPTH_BITS;
	val GL_STENCIL_BITS = GL.GL_STENCIL_BITS;
	val GL_POLYGON_OFFSET_UNITS = GL.GL_POLYGON_OFFSET_UNITS;
	val GL_POLYGON_OFFSET_FACTOR = GL.GL_POLYGON_OFFSET_FACTOR;
	val GL_TEXTURE_BINDING_2D = GL.GL_TEXTURE_BINDING_2D;
	val GL_SAMPLE_BUFFERS = GL.GL_SAMPLE_BUFFERS;
	val GL_SAMPLES = GL.GL_SAMPLES;
	val GL_SAMPLE_COVERAGE_VALUE = GL.GL_SAMPLE_COVERAGE_VALUE;
	val GL_SAMPLE_COVERAGE_INVERT = GL.GL_SAMPLE_COVERAGE_INVERT;
	val GL_NUM_COMPRESSED_TEXTURE_FORMATS = GL.GL_NUM_COMPRESSED_TEXTURE_FORMATS;
	val GL_COMPRESSED_TEXTURE_FORMATS = GL.GL_COMPRESSED_TEXTURE_FORMATS;
	val GL_DONT_CARE = GL.GL_DONT_CARE;
	val GL_FASTEST = GL.GL_FASTEST;
	val GL_NICEST = GL.GL_NICEST;
	val GL_GENERATE_MIPMAP_HINT = GL.GL_GENERATE_MIPMAP_HINT;
	val GL_BYTE = GL.GL_BYTE;
	val GL_UNSIGNED_BYTE = GL.GL_UNSIGNED_BYTE;
	val GL_SHORT = GL.GL_SHORT;
	val GL_UNSIGNED_SHORT = GL.GL_UNSIGNED_SHORT;
	val GL_FLOAT = GL.GL_FLOAT;
	val GL_FIXED = GL.GL_FIXED;
	val GL_ALPHA = GL.GL_ALPHA;
	val GL_RGB = GL.GL_RGB;
	val GL_RGBA = GL.GL_RGBA;
	val GL_LUMINANCE = GL.GL_LUMINANCE;
	val GL_LUMINANCE_ALPHA = GL.GL_LUMINANCE_ALPHA;
	val GL_UNSIGNED_SHORT_4_4_4_4 = GL.GL_UNSIGNED_SHORT_4_4_4_4;
	val GL_UNSIGNED_SHORT_5_5_5_1 = GL.GL_UNSIGNED_SHORT_5_5_5_1;
	val GL_UNSIGNED_SHORT_5_6_5 = GL.GL_UNSIGNED_SHORT_5_6_5;
	val GL_NEVER = GL.GL_NEVER;
	val GL_LESS = GL.GL_LESS;
	val GL_EQUAL = GL.GL_EQUAL;
	val GL_LEQUAL = GL.GL_LEQUAL;
	val GL_GREATER = GL.GL_GREATER;
	val GL_NOTEQUAL = GL.GL_NOTEQUAL;
	val GL_GEQUAL = GL.GL_GEQUAL;
	val GL_ALWAYS = GL.GL_ALWAYS;
	val GL_KEEP = GL.GL_KEEP;
	val GL_REPLACE = GL.GL_REPLACE;
	val GL_INCR = GL.GL_INCR;
	val GL_DECR = GL.GL_DECR;
	val GL_INVERT = GL.GL_INVERT;
	val GL_INCR_WRAP = GL.GL_INCR_WRAP;
	val GL_DECR_WRAP = GL.GL_DECR_WRAP;
	val GL_VENDOR = GL.GL_VENDOR;
	val GL_RENDERER = GL.GL_RENDERER;
	val GL_VERSION = GL.GL_VERSION;
	val GL_EXTENSIONS = GL.GL_EXTENSIONS;
	val GL_NEAREST = GL.GL_NEAREST;
	val GL_LINEAR = GL.GL_LINEAR;
	val GL_NEAREST_MIPMAP_NEAREST = GL.GL_NEAREST_MIPMAP_NEAREST;
	val GL_LINEAR_MIPMAP_NEAREST = GL.GL_LINEAR_MIPMAP_NEAREST;
	val GL_NEAREST_MIPMAP_LINEAR = GL.GL_NEAREST_MIPMAP_LINEAR;
	val GL_LINEAR_MIPMAP_LINEAR = GL.GL_LINEAR_MIPMAP_LINEAR;
	val GL_TEXTURE_MAG_FILTER = GL.GL_TEXTURE_MAG_FILTER;
	val GL_TEXTURE_MIN_FILTER = GL.GL_TEXTURE_MIN_FILTER;
	val GL_TEXTURE_WRAP_S = GL.GL_TEXTURE_WRAP_S;
	val GL_TEXTURE_WRAP_T = GL.GL_TEXTURE_WRAP_T;
	val GL_TEXTURE = GL.GL_TEXTURE;
	val GL_TEXTURE_CUBE_MAP = GL.GL_TEXTURE_CUBE_MAP;
	val GL_TEXTURE_BINDING_CUBE_MAP = GL.GL_TEXTURE_BINDING_CUBE_MAP;
	val GL_TEXTURE_CUBE_MAP_POSITIVE_X = GL.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
	val GL_TEXTURE_CUBE_MAP_NEGATIVE_X = GL.GL_TEXTURE_CUBE_MAP_NEGATIVE_X;
	val GL_TEXTURE_CUBE_MAP_POSITIVE_Y = GL.GL_TEXTURE_CUBE_MAP_POSITIVE_Y;
	val GL_TEXTURE_CUBE_MAP_NEGATIVE_Y = GL.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y;
	val GL_TEXTURE_CUBE_MAP_POSITIVE_Z = GL.GL_TEXTURE_CUBE_MAP_POSITIVE_Z;
	val GL_TEXTURE_CUBE_MAP_NEGATIVE_Z = GL.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z;
	val GL_MAX_CUBE_MAP_TEXTURE_SIZE = GL.GL_MAX_CUBE_MAP_TEXTURE_SIZE;
	val GL_TEXTURE0 = GL.GL_TEXTURE0;
	val GL_TEXTURE1 = GL.GL_TEXTURE1;
	val GL_TEXTURE2 = GL.GL_TEXTURE2;
	val GL_TEXTURE3 = GL.GL_TEXTURE3;
	val GL_TEXTURE4 = GL.GL_TEXTURE4;
	val GL_TEXTURE5 = GL.GL_TEXTURE5;
	val GL_TEXTURE6 = GL.GL_TEXTURE6;
	val GL_TEXTURE7 = GL.GL_TEXTURE7;
	val GL_TEXTURE8 = GL.GL_TEXTURE8;
	val GL_TEXTURE9 = GL.GL_TEXTURE9;
	val GL_TEXTURE10 = GL.GL_TEXTURE10;
	val GL_TEXTURE11 = GL.GL_TEXTURE11;
	val GL_TEXTURE12 = GL.GL_TEXTURE12;
	val GL_TEXTURE13 = GL.GL_TEXTURE13;
	val GL_TEXTURE14 = GL.GL_TEXTURE14;
	val GL_TEXTURE15 = GL.GL_TEXTURE15;
	val GL_TEXTURE16 = GL.GL_TEXTURE16;
	val GL_TEXTURE17 = GL.GL_TEXTURE17;
	val GL_TEXTURE18 = GL.GL_TEXTURE18;
	val GL_TEXTURE19 = GL.GL_TEXTURE19;
	val GL_TEXTURE20 = GL.GL_TEXTURE20;
	val GL_TEXTURE21 = GL.GL_TEXTURE21;
	val GL_TEXTURE22 = GL.GL_TEXTURE22;
	val GL_TEXTURE23 = GL.GL_TEXTURE23;
	val GL_TEXTURE24 = GL.GL_TEXTURE24;
	val GL_TEXTURE25 = GL.GL_TEXTURE25;
	val GL_TEXTURE26 = GL.GL_TEXTURE26;
	val GL_TEXTURE27 = GL.GL_TEXTURE27;
	val GL_TEXTURE28 = GL.GL_TEXTURE28;
	val GL_TEXTURE29 = GL.GL_TEXTURE29;
	val GL_TEXTURE30 = GL.GL_TEXTURE30;
	val GL_TEXTURE31 = GL.GL_TEXTURE31;
	val GL_ACTIVE_TEXTURE = GL.GL_ACTIVE_TEXTURE;
	val GL_REPEAT = GL.GL_REPEAT;
	val GL_CLAMP_TO_EDGE = GL.GL_CLAMP_TO_EDGE;
	val GL_MIRRORED_REPEAT = GL.GL_MIRRORED_REPEAT;
	val GL_IMPLEMENTATION_COLOR_READ_TYPE = GL.GL_IMPLEMENTATION_COLOR_READ_TYPE;
	val GL_IMPLEMENTATION_COLOR_READ_FORMAT = GL.GL_IMPLEMENTATION_COLOR_READ_FORMAT;
	val GL_FRAMEBUFFER = GL.GL_FRAMEBUFFER;
	val GL_RENDERBUFFER = GL.GL_RENDERBUFFER;
	val GL_RGBA4 = GL.GL_RGBA4;
	val GL_RGB5_A1 = GL.GL_RGB5_A1;
	val GL_RGB565 = GL.GL_RGB565;
	val GL_DEPTH_COMPONENT16 = GL.GL_DEPTH_COMPONENT16;
	val GL_STENCIL_INDEX8 = GL.GL_STENCIL_INDEX8;
	val GL_RENDERBUFFER_WIDTH = GL.GL_RENDERBUFFER_WIDTH;
	val GL_RENDERBUFFER_HEIGHT = GL.GL_RENDERBUFFER_HEIGHT;
	val GL_RENDERBUFFER_INTERNAL_FORMAT = GL.GL_RENDERBUFFER_INTERNAL_FORMAT;
	val GL_RENDERBUFFER_RED_SIZE = GL.GL_RENDERBUFFER_RED_SIZE;
	val GL_RENDERBUFFER_GREEN_SIZE = GL.GL_RENDERBUFFER_GREEN_SIZE;
	val GL_RENDERBUFFER_BLUE_SIZE = GL.GL_RENDERBUFFER_BLUE_SIZE;
	val GL_RENDERBUFFER_ALPHA_SIZE = GL.GL_RENDERBUFFER_ALPHA_SIZE;
	val GL_RENDERBUFFER_DEPTH_SIZE = GL.GL_RENDERBUFFER_DEPTH_SIZE;
	val GL_RENDERBUFFER_STENCIL_SIZE = GL.GL_RENDERBUFFER_STENCIL_SIZE;
	val GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE = GL.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE;
	val GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME = GL.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME;
	val GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL = GL.GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL;
	val GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE = GL.GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE;
	val GL_COLOR_ATTACHMENT0 = GL.GL_COLOR_ATTACHMENT0;
	val GL_DEPTH_ATTACHMENT = GL.GL_DEPTH_ATTACHMENT;
	val GL_STENCIL_ATTACHMENT = GL.GL_STENCIL_ATTACHMENT;
	val GL_FRAMEBUFFER_COMPLETE = GL.GL_FRAMEBUFFER_COMPLETE;
	val GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT = GL.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT;
	val GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = GL.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT;
	val GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS = GL.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS;
	val GL_FRAMEBUFFER_INCOMPLETE_FORMATS = GL.GL_FRAMEBUFFER_INCOMPLETE_FORMATS;
	val GL_FRAMEBUFFER_UNSUPPORTED = GL.GL_FRAMEBUFFER_UNSUPPORTED;
	val GL_FRAMEBUFFER_BINDING = GL.GL_FRAMEBUFFER_BINDING;
	val GL_RENDERBUFFER_BINDING = GL.GL_RENDERBUFFER_BINDING;
	val GL_MAX_RENDERBUFFER_SIZE = GL.GL_MAX_RENDERBUFFER_SIZE;
	val GL_INVALID_FRAMEBUFFER_OPERATION = GL.GL_INVALID_FRAMEBUFFER_OPERATION;
	val GL_HALF_FLOAT = GL.GL_HALF_FLOAT;
	val GL_BUFFER_MAPPED = GL.GL_BUFFER_MAPPED;
	val GL_BUFFER_MAP_POINTER = GL.GL_BUFFER_MAP_POINTER;
	val GL_RGB8 = GL.GL_RGB8;
	val GL_RGBA8 = GL.GL_RGBA8;
	val GL_R11F_G11F_B10F = GL.GL_R11F_G11F_B10F;
	val GL_UNSIGNED_INT_10F_11F_11F_REV = GL.GL_UNSIGNED_INT_10F_11F_11F_REV;
	val GL_RGBA_SIGNED_COMPONENTS = GL.GL_RGBA_SIGNED_COMPONENTS;
	val GL_TEXTURE_2D_ARRAY = GL.GL_TEXTURE_2D_ARRAY;
	val GL_SAMPLER_2D_ARRAY = GL.GL_SAMPLER_2D_ARRAY;
	val GL_TEXTURE_BINDING_2D_ARRAY = GL.GL_TEXTURE_BINDING_2D_ARRAY;
	val GL_MAX_ARRAY_TEXTURE_LAYERS = GL.GL_MAX_ARRAY_TEXTURE_LAYERS;
	val GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER = GL.GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER;
	val GL_COMPRESSED_RGB_S3TC_DXT1_EXT = GL.GL_COMPRESSED_RGB_S3TC_DXT1_EXT;
	val GL_COMPRESSED_RGBA_S3TC_DXT1_EXT = GL.GL_COMPRESSED_RGBA_S3TC_DXT1_EXT;
	val GL_COMPRESSED_RGBA_S3TC_DXT3_EXT = GL.GL_COMPRESSED_RGBA_S3TC_DXT3_EXT;
	val GL_COMPRESSED_RGBA_S3TC_DXT5_EXT = GL.GL_COMPRESSED_RGBA_S3TC_DXT5_EXT;
	val GL_TEXTURE_MAX_ANISOTROPY_EXT = GL.GL_TEXTURE_MAX_ANISOTROPY_EXT;
	val GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT = GL.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT;
	val GL_AND = GL.GL_AND;
	val GL_AND_INVERTED = GL.GL_AND_INVERTED;
	val GL_AND_REVERSE = GL.GL_AND_REVERSE;
	val GL_BLEND_DST = GL.GL_BLEND_DST;
	val GL_BLEND_SRC = GL.GL_BLEND_SRC;
	val GL_BUFFER_ACCESS = GL.GL_BUFFER_ACCESS;
	val GL_CLEAR = GL.GL_CLEAR;
	val GL_COLOR_LOGIC_OP = GL.GL_COLOR_LOGIC_OP;
	val GL_COPY = GL.GL_COPY;
	val GL_COPY_INVERTED = GL.GL_COPY_INVERTED;
	val GL_DEPTH_COMPONENT24 = GL.GL_DEPTH_COMPONENT24;
	val GL_DEPTH_COMPONENT32 = GL.GL_DEPTH_COMPONENT32;
	val GL_EQUIV = GL.GL_EQUIV;
	val GL_LINE_SMOOTH = GL.GL_LINE_SMOOTH;
	val GL_LINE_SMOOTH_HINT = GL.GL_LINE_SMOOTH_HINT;
	val GL_LOGIC_OP_MODE = GL.GL_LOGIC_OP_MODE;
	val GL_MULTISAMPLE = GL.GL_MULTISAMPLE;
	val GL_NAND = GL.GL_NAND;
	val GL_NOOP = GL.GL_NOOP;
	val GL_NOR = GL.GL_NOR;
	val GL_OR = GL.GL_OR;
	val GL_OR_INVERTED = GL.GL_OR_INVERTED;
	val GL_OR_REVERSE = GL.GL_OR_REVERSE;
	val GL_POINT_FADE_THRESHOLD_SIZE = GL.GL_POINT_FADE_THRESHOLD_SIZE;
	val GL_POINT_SIZE = GL.GL_POINT_SIZE;
	val GL_SAMPLE_ALPHA_TO_ONE = GL.GL_SAMPLE_ALPHA_TO_ONE;
	val GL_SET = GL.GL_SET;
	val GL_SMOOTH_LINE_WIDTH_RANGE = GL.GL_SMOOTH_LINE_WIDTH_RANGE;
	val GL_SMOOTH_POINT_SIZE_RANGE = GL.GL_SMOOTH_POINT_SIZE_RANGE;
	val GL_STENCIL_INDEX1 = GL.GL_STENCIL_INDEX1;
	val GL_STENCIL_INDEX4 = GL.GL_STENCIL_INDEX4;
	val GL_WRITE_ONLY = GL.GL_WRITE_ONLY;
	val GL_XOR = GL.GL_XOR;


	def glActiveTexture(arg0:Int):Unit = {
		GLContext.gl.glActiveTexture(arg0);
	}
	def glBindBuffer(arg0:Int, arg1:Int):Unit = {
		GLContext.gl.glBindBuffer(arg0, arg1);
	}
	def glBindFramebuffer(arg0:Int, arg1:Int):Unit = {
		GLContext.gl.glBindFramebuffer(arg0, arg1);
	}
	def glBindRenderbuffer(arg0:Int, arg1:Int):Unit = {
		GLContext.gl.glBindRenderbuffer(arg0, arg1);
	}
	def glBindTexture(arg0:Int, arg1:Int):Unit = {
		GLContext.gl.glBindTexture(arg0, arg1);
	}
	def glBlendEquation(arg0:Int):Unit = {
		GLContext.gl.glBlendEquation(arg0);
	}
	def glBlendEquationSeparate(arg0:Int, arg1:Int):Unit = {
		GLContext.gl.glBlendEquationSeparate(arg0, arg1);
	}
	def glBlendFunc(arg0:Int, arg1:Int):Unit = {
		GLContext.gl.glBlendFunc(arg0, arg1);
	}
	def glBlendFuncSeparate(arg0:Int, arg1:Int, arg2:Int, arg3:Int):Unit = {
		GLContext.gl.glBlendFuncSeparate(arg0, arg1, arg2, arg3);
	}
	def glBufferData(arg0:Int, arg1:Int, arg2:java.nio.Buffer, arg3:Int):Unit = {
		GLContext.gl.glBufferData(arg0, arg1, arg2, arg3);
	}
	def glBufferSubData(arg0:Int, arg1:Int, arg2:Int, arg3:java.nio.Buffer):Unit = {
		GLContext.gl.glBufferSubData(arg0, arg1, arg2, arg3);
	}
	def glCheckFramebufferStatus(arg0:Int):Int = {
		GLContext.gl.glCheckFramebufferStatus(arg0);
	}
	def glClear(arg0:Int):Unit = {
		GLContext.gl.glClear(arg0);
	}
	def glClearColor(arg0:Float, arg1:Float, arg2:Float, arg3:Float):Unit = {
		GLContext.gl.glClearColor(arg0, arg1, arg2, arg3);
	}
	def glClearDepthf(arg0:Float):Unit = {
		GLContext.gl.glClearDepthf(arg0);
	}
	def glClearStencil(arg0:Int):Unit = {
		GLContext.gl.glClearStencil(arg0);
	}
	def glColorMask(arg0:Boolean, arg1:Boolean, arg2:Boolean, arg3:Boolean):Unit = {
		GLContext.gl.glColorMask(arg0, arg1, arg2, arg3);
	}
	def glCompressedTexImage2D(arg0:Int, arg1:Int, arg2:Int, arg3:Int, arg4:Int, arg5:Int, arg6:Int, arg7:Long):Unit = {
		GLContext.gl.glCompressedTexImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	}
	def glCompressedTexImage2D(arg0:Int, arg1:Int, arg2:Int, arg3:Int, arg4:Int, arg5:Int, arg6:Int, arg7:java.nio.Buffer):Unit = {
		GLContext.gl.glCompressedTexImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	}
	def glCompressedTexSubImage2D(arg0:Int, arg1:Int, arg2:Int, arg3:Int, arg4:Int, arg5:Int, arg6:Int, arg7:Int, arg8:java.nio.Buffer):Unit = {
		GLContext.gl.glCompressedTexSubImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	}
	def glCompressedTexSubImage2D(arg0:Int, arg1:Int, arg2:Int, arg3:Int, arg4:Int, arg5:Int, arg6:Int, arg7:Int, arg8:Long):Unit = {
		GLContext.gl.glCompressedTexSubImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	}
	def glCopyTexImage2D(arg0:Int, arg1:Int, arg2:Int, arg3:Int, arg4:Int, arg5:Int, arg6:Int, arg7:Int):Unit = {
		GLContext.gl.glCopyTexImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	}
	def glCopyTexSubImage2D(arg0:Int, arg1:Int, arg2:Int, arg3:Int, arg4:Int, arg5:Int, arg6:Int, arg7:Int):Unit = {
		GLContext.gl.glCopyTexSubImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	}
	def glCullFace(arg0:Int):Unit = {
		GLContext.gl.glCullFace(arg0);
	}
	def glDeleteBuffers(arg0:Int, arg1:Array[Int], arg2:Int):Unit = {
		GLContext.gl.glDeleteBuffers(arg0, arg1, arg2);
	}
	def glDeleteBuffers(arg0:Int, arg1:java.nio.IntBuffer):Unit = {
		GLContext.gl.glDeleteBuffers(arg0, arg1);
	}
	def glDeleteFramebuffers(arg0:Int, arg1:java.nio.IntBuffer):Unit = {
		GLContext.gl.glDeleteFramebuffers(arg0, arg1);
	}
	def glDeleteFramebuffers(arg0:Int, arg1:Array[Int], arg2:Int):Unit = {
		GLContext.gl.glDeleteFramebuffers(arg0, arg1, arg2);
	}
	def glDeleteRenderbuffers(arg0:Int, arg1:Array[Int], arg2:Int):Unit = {
		GLContext.gl.glDeleteRenderbuffers(arg0, arg1, arg2);
	}
	def glDeleteRenderbuffers(arg0:Int, arg1:java.nio.IntBuffer):Unit = {
		GLContext.gl.glDeleteRenderbuffers(arg0, arg1);
	}
	def glDeleteTextures(arg0:Int, arg1:Array[Int], arg2:Int):Unit = {
		GLContext.gl.glDeleteTextures(arg0, arg1, arg2);
	}
	def glDeleteTextures(arg0:Int, arg1:java.nio.IntBuffer):Unit = {
		GLContext.gl.glDeleteTextures(arg0, arg1);
	}
	def glDepthFunc(arg0:Int):Unit = {
		GLContext.gl.glDepthFunc(arg0);
	}
	def glDepthMask(arg0:Boolean):Unit = {
		GLContext.gl.glDepthMask(arg0);
	}
	def glDepthRangef(arg0:Float, arg1:Float):Unit = {
		GLContext.gl.glDepthRangef(arg0, arg1);
	}
	def glDisable(arg0:Int):Unit = {
		GLContext.gl.glDisable(arg0);
	}
	def glDrawArrays(arg0:Int, arg1:Int, arg2:Int):Unit = {
		GLContext.gl.glDrawArrays(arg0, arg1, arg2);
	}
	def glDrawElements(arg0:Int, arg1:Int, arg2:Int, arg3:Long):Unit = {
		GLContext.gl.glDrawElements(arg0, arg1, arg2, arg3);
	}
	def glDrawElements(arg0:Int, arg1:Int, arg2:Int, arg3:java.nio.Buffer):Unit = {
		GLContext.gl.glDrawElements(arg0, arg1, arg2, arg3);
	}
	def glEnable(arg0:Int):Unit = {
		GLContext.gl.glEnable(arg0);
	}
	def glFinish():Unit = {
		GLContext.gl.glFinish();
	}
	def glFlush():Unit = {
		GLContext.gl.glFlush();
	}
	def glFramebufferRenderbuffer(arg0:Int, arg1:Int, arg2:Int, arg3:Int):Unit = {
		GLContext.gl.glFramebufferRenderbuffer(arg0, arg1, arg2, arg3);
	}
	def glFramebufferTexture2D(arg0:Int, arg1:Int, arg2:Int, arg3:Int, arg4:Int):Unit = {
		GLContext.gl.glFramebufferTexture2D(arg0, arg1, arg2, arg3, arg4);
	}
	def glFrontFace(arg0:Int):Unit = {
		GLContext.gl.glFrontFace(arg0);
	}
	def glGenBuffers(arg0:Int, arg1:Array[Int], arg2:Int):Unit = {
		GLContext.gl.glGenBuffers(arg0, arg1, arg2);
	}
	def glGenBuffers(arg0:Int, arg1:java.nio.IntBuffer):Unit = {
		GLContext.gl.glGenBuffers(arg0, arg1);
	}
	def glGenFramebuffers(arg0:Int, arg1:java.nio.IntBuffer):Unit = {
		GLContext.gl.glGenFramebuffers(arg0, arg1);
	}
	def glGenFramebuffers(arg0:Int, arg1:Array[Int], arg2:Int):Unit = {
		GLContext.gl.glGenFramebuffers(arg0, arg1, arg2);
	}
	def glGenRenderbuffers(arg0:Int, arg1:Array[Int], arg2:Int):Unit = {
		GLContext.gl.glGenRenderbuffers(arg0, arg1, arg2);
	}
	def glGenRenderbuffers(arg0:Int, arg1:java.nio.IntBuffer):Unit = {
		GLContext.gl.glGenRenderbuffers(arg0, arg1);
	}
	def glGenTextures(arg0:Int, arg1:Array[Int], arg2:Int):Unit = {
		GLContext.gl.glGenTextures(arg0, arg1, arg2);
	}
	def glGenTextures(arg0:Int, arg1:java.nio.IntBuffer):Unit = {
		GLContext.gl.glGenTextures(arg0, arg1);
	}
	def glGenerateMipmap(arg0:Int):Unit = {
		GLContext.gl.glGenerateMipmap(arg0);
	}
	def glGetBooleanv(arg0:Int, arg1:Array[Byte], arg2:Int):Unit = {
		GLContext.gl.glGetBooleanv(arg0, arg1, arg2);
	}
	def glGetBooleanv(arg0:Int, arg1:java.nio.ByteBuffer):Unit = {
		GLContext.gl.glGetBooleanv(arg0, arg1);
	}
	def glGetBufferParameteriv(arg0:Int, arg1:Int, arg2:Array[Int], arg3:Int):Unit = {
		GLContext.gl.glGetBufferParameteriv(arg0, arg1, arg2, arg3);
	}
	def glGetBufferParameteriv(arg0:Int, arg1:Int, arg2:java.nio.IntBuffer):Unit = {
		GLContext.gl.glGetBufferParameteriv(arg0, arg1, arg2);
	}
	def glGetError():Int = {
		GLContext.gl.glGetError();
	}
	def glGetFramebufferAttachmentParameteriv(arg0:Int, arg1:Int, arg2:Int, arg3:Array[Int], arg4:Int):Unit = {
		GLContext.gl.glGetFramebufferAttachmentParameteriv(arg0, arg1, arg2, arg3, arg4);
	}
	def glGetFramebufferAttachmentParameteriv(arg0:Int, arg1:Int, arg2:Int, arg3:java.nio.IntBuffer):Unit = {
		GLContext.gl.glGetFramebufferAttachmentParameteriv(arg0, arg1, arg2, arg3);
	}
	def glGetRenderbufferParameteriv(arg0:Int, arg1:Int, arg2:Array[Int], arg3:Int):Unit = {
		GLContext.gl.glGetRenderbufferParameteriv(arg0, arg1, arg2, arg3);
	}
	def glGetRenderbufferParameteriv(arg0:Int, arg1:Int, arg2:java.nio.IntBuffer):Unit = {
		GLContext.gl.glGetRenderbufferParameteriv(arg0, arg1, arg2);
	}
	def glGetString(arg0:Int):java.lang.String = {
		GLContext.gl.glGetString(arg0);
	}
	def glGetTexParameterfv(arg0:Int, arg1:Int, arg2:Array[Float], arg3:Int):Unit = {
		GLContext.gl.glGetTexParameterfv(arg0, arg1, arg2, arg3);
	}
	def glGetTexParameterfv(arg0:Int, arg1:Int, arg2:java.nio.FloatBuffer):Unit = {
		GLContext.gl.glGetTexParameterfv(arg0, arg1, arg2);
	}
	def glGetTexParameteriv(arg0:Int, arg1:Int, arg2:Array[Int], arg3:Int):Unit = {
		GLContext.gl.glGetTexParameteriv(arg0, arg1, arg2, arg3);
	}
	def glGetTexParameteriv(arg0:Int, arg1:Int, arg2:java.nio.IntBuffer):Unit = {
		GLContext.gl.glGetTexParameteriv(arg0, arg1, arg2);
	}
	def glHint(arg0:Int, arg1:Int):Unit = {
		GLContext.gl.glHint(arg0, arg1);
	}
	def glIsBuffer(arg0:Int):Boolean = {
		GLContext.gl.glIsBuffer(arg0);
	}
	def glIsEnabled(arg0:Int):Boolean = {
		GLContext.gl.glIsEnabled(arg0);
	}
	def glIsFramebuffer(arg0:Int):Boolean = {
		GLContext.gl.glIsFramebuffer(arg0);
	}
	def glIsRenderbuffer(arg0:Int):Boolean = {
		GLContext.gl.glIsRenderbuffer(arg0);
	}
	def glIsTexture(arg0:Int):Boolean = {
		GLContext.gl.glIsTexture(arg0);
	}
	def glLineWidth(arg0:Float):Unit = {
		GLContext.gl.glLineWidth(arg0);
	}
	def glMapBuffer(arg0:Int, arg1:Int):java.nio.ByteBuffer = {
		GLContext.gl.glMapBuffer(arg0, arg1);
	}
	def glPixelStorei(arg0:Int, arg1:Int):Unit = {
		GLContext.gl.glPixelStorei(arg0, arg1);
	}
	def glPolygonOffset(arg0:Float, arg1:Float):Unit = {
		GLContext.gl.glPolygonOffset(arg0, arg1);
	}
	def glReadPixels(arg0:Int, arg1:Int, arg2:Int, arg3:Int, arg4:Int, arg5:Int, arg6:Long):Unit = {
		GLContext.gl.glReadPixels(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
	}
	def glReadPixels(arg0:Int, arg1:Int, arg2:Int, arg3:Int, arg4:Int, arg5:Int, arg6:java.nio.Buffer):Unit = {
		GLContext.gl.glReadPixels(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
	}
	def glRenderbufferStorage(arg0:Int, arg1:Int, arg2:Int, arg3:Int):Unit = {
		GLContext.gl.glRenderbufferStorage(arg0, arg1, arg2, arg3);
	}
	def glSampleCoverage(arg0:Float, arg1:Boolean):Unit = {
		GLContext.gl.glSampleCoverage(arg0, arg1);
	}
	def glScissor(arg0:Int, arg1:Int, arg2:Int, arg3:Int):Unit = {
		GLContext.gl.glScissor(arg0, arg1, arg2, arg3);
	}
	def glStencilFunc(arg0:Int, arg1:Int, arg2:Int):Unit = {
		GLContext.gl.glStencilFunc(arg0, arg1, arg2);
	}
	def glStencilMask(arg0:Int):Unit = {
		GLContext.gl.glStencilMask(arg0);
	}
	def glStencilOp(arg0:Int, arg1:Int, arg2:Int):Unit = {
		GLContext.gl.glStencilOp(arg0, arg1, arg2);
	}
	def glTexImage2D(arg0:Int, arg1:Int, arg2:Int, arg3:Int, arg4:Int, arg5:Int, arg6:Int, arg7:Int, arg8:Long):Unit = {
		GLContext.gl.glTexImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	}
	def glTexImage2D(arg0:Int, arg1:Int, arg2:Int, arg3:Int, arg4:Int, arg5:Int, arg6:Int, arg7:Int, arg8:java.nio.Buffer):Unit = {
		GLContext.gl.glTexImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	}
	def glTexParameterf(arg0:Int, arg1:Int, arg2:Float):Unit = {
		GLContext.gl.glTexParameterf(arg0, arg1, arg2);
	}
	def glTexParameterfv(arg0:Int, arg1:Int, arg2:java.nio.FloatBuffer):Unit = {
		GLContext.gl.glTexParameterfv(arg0, arg1, arg2);
	}
	def glTexParameterfv(arg0:Int, arg1:Int, arg2:Array[Float], arg3:Int):Unit = {
		GLContext.gl.glTexParameterfv(arg0, arg1, arg2, arg3);
	}
	def glTexParameteri(arg0:Int, arg1:Int, arg2:Int):Unit = {
		GLContext.gl.glTexParameteri(arg0, arg1, arg2);
	}
	def glTexParameteriv(arg0:Int, arg1:Int, arg2:Array[Int], arg3:Int):Unit = {
		GLContext.gl.glTexParameteriv(arg0, arg1, arg2, arg3);
	}
	def glTexParameteriv(arg0:Int, arg1:Int, arg2:java.nio.IntBuffer):Unit = {
		GLContext.gl.glTexParameteriv(arg0, arg1, arg2);
	}
	def glTexSubImage2D(arg0:Int, arg1:Int, arg2:Int, arg3:Int, arg4:Int, arg5:Int, arg6:Int, arg7:Int, arg8:Long):Unit = {
		GLContext.gl.glTexSubImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	}
	def glTexSubImage2D(arg0:Int, arg1:Int, arg2:Int, arg3:Int, arg4:Int, arg5:Int, arg6:Int, arg7:Int, arg8:java.nio.Buffer):Unit = {
		GLContext.gl.glTexSubImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	}
	def glUnmapBuffer(arg0:Int):Boolean = {
		GLContext.gl.glUnmapBuffer(arg0);
	}
	def glViewport(arg0:Int, arg1:Int, arg2:Int, arg3:Int):Unit = {
		GLContext.gl.glViewport(arg0, arg1, arg2, arg3);
	}
	def glClearDepth(arg0:Double):Unit = {
		GLContext.gl.glClearDepth(arg0);
	}
	def glDepthRange(arg0:Double, arg1:Double):Unit = {
		GLContext.gl.glDepthRange(arg0, arg1);
	}
	def glGetBoundBuffer(arg0:Int):Int = {
		GLContext.gl.glGetBoundBuffer(arg0);
	}
	def glIsVBOArrayEnabled():Boolean = {
		GLContext.gl.glIsVBOArrayEnabled();
	}
	def glIsVBOElementEnabled():Boolean = {
		GLContext.gl.glIsVBOElementEnabled();
	}
}