#include "ruby.h"
#include "rubyspec.h"

#ifdef __cplusplus
extern "C" {
#endif

// Tests that Sulong does not force ptr to native
char* function_returning_char_ptr(char* ptr) {
  return ptr;
}

static VALUE string_ptr(VALUE self, VALUE str) {
  char* ptr = function_returning_char_ptr(RSTRING_PTR(str));
  char cstring[] = { ptr[0], 0 };
  return rb_str_new_cstr(cstring);
}

static VALUE string_ptr_return_address(VALUE self, VALUE str) {
  char* ptr = RSTRING_PTR(str);
  return LONG2NUM((long) ptr);
}

void Init_truffleruby_string_spec(void) {
  VALUE cls;
  cls = rb_define_class("CApiTruffleStringSpecs", rb_cObject);
  rb_define_method(cls, "string_ptr", string_ptr, 1);
  rb_define_method(cls, "string_ptr_return_address", string_ptr_return_address, 1);
}

#ifdef __cplusplus
}
#endif
