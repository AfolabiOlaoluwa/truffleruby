# Optcarrot Example

First, clone the optcarrot repository:

```bash
$ git clone https://github.com/mame/optcarrot.git
$ cd optcarrot
```

You will need `SDL2` to be installed. Then you can play Lan Master with the
following command:

```bash
$ path/to/graalvm/bin/ruby --jvm bin/optcarrot --sdl2 --audio=none examples/Lan_Master.nes
```

On macOS, you might need an extra flag for the GUI to appear:

```bash
$ path/to/graalvm/bin/ruby --jvm --vm.XstartOnFirstThread bin/optcarrot --sdl2 --audio=none examples/Lan_Master.nes
```

If you have a local checkout of TruffleRuby, you can also use the version of
OptCarrot under `bench/optcarrot`. See `bench/optcarrot/README.md` for details.

More information can be found in this [blog post](https://eregon.me/blog/2016/11/28/optcarrot.html).
