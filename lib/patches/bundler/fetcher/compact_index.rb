module Bundler
  class Fetcher
    class CompactIndex < Base
      private
      def md5_available?
        false
      end
    end
  end
end
