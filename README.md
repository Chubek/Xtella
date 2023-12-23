# Xtella: a Language for Text Templating

Xtella is a language similar to Perl, but with ML-like syntax and abilities. I am currently working on the virtual machine. A preliminary grammar in ANTLR4 syntax has been written.

This is how Xtella looks like (at the moment)

```
let myFunc a, b, c -> a = {2}; print @a; end;;
```

Not very good! I am focusing mostly on the VM right now. The grammar shall conform to the VM, not the other way around. I may make it more, or less, or not similar to Perl at all. Who knows?
