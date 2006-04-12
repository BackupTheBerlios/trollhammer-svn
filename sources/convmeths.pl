#! /usr/bin/perl

while (<>) {
    if($_ =~ /.+\((.+:.+)*\) : .+$/) {
        $input = $_;
        $input =~ s/(.+)\((.*)\) : (.+)$/\3 \1(\2) {/;
#        $input =~ s/(\w.+) : (\w.+)/\2 \1/g;
        print $input;
    }
}
