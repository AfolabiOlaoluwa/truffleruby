module Truffle::Patching
  TRUFFLE_PATCHES_DIRECTORY = "#{Truffle::Boot.ruby_home}/lib/patches"
  TRUFFLE_PATCHES           = Dir.glob("#{TRUFFLE_PATCHES_DIRECTORY}/**/*.rb").
      select { |path| File.file? path }.
      map { |path| [path[(TRUFFLE_PATCHES_DIRECTORY.size + 1)..-4], true] }.
      to_h
end

module Kernel

  private

  alias_method :require_without_truffle_patching, :gem_original_require

  def gem_original_require(path)
    required = begin
      require_without_truffle_patching path
    rescue LoadError
      if Truffle::Patching::TRUFFLE_PATCHES[path]
        true # Pretend the file required so we attempt patching with something that should avoid the LoadError (e.g., replacing code using C extensions).
      else
        raise
      end
    end

    if required && Truffle::Patching::TRUFFLE_PATCHES[path]
      Truffle::System.log :PATCH, "applying #{path}"
      require_without_truffle_patching "#{Truffle::Patching::TRUFFLE_PATCHES_DIRECTORY}/#{path}.rb"
    end

    required
  end
end

class Module

  alias_method :autoload_without_truffle_patching, :autoload
  private :autoload_without_truffle_patching

  def autoload(const, path)
    if Truffle::Patching::TRUFFLE_PATCHES[path]
      require path
    else
      autoload_without_truffle_patching const, path
    end
  end
end
