#! /usr/bin/perl

while (<>) {
    if($_ =~ /.+\((.+:.+)*\) : .+$/) {
        $input = $_;
        $input =~ s/(.+)\((.*)\) : (.+)$/\3 \1(\2) {/;
        $prev = "";

        # itération au point fixe puisqu'on ne peut pas corriger les attributs
        # un à un, bizarrement. BANZAI. Qu'un Perl Monk trouve mieux ! 
        while ($prev ne $input) {
            $prev = $input;
            $input =~ s/(\(|,)\s*(\w+)\s*:\s*([\w\[\]]+)\s*(\)|,)/\1 \3 \2\4/ ;
        }
        # la dernière correction, le "( " => "("
        $input =~ s/\( /(/ ;
        print $input."\n}\n\n";
    }
}
