#!/usr/bin/env bash

source test/truffle/common.sh.inc

jt ruby --vm.Dgraal.TruffleCompilationExceptionsAreFatal=true test/truffle/compiler/osr/osr.rb
