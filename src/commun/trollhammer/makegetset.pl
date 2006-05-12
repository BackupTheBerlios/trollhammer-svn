#! /usr/bin/perl

my @un;
my @deux;
my @trois;

while (<>) {
    $c1 = $_;
    $c2 = $_;
    $c3 = $_;

    $c2 =~ s/private (\w+) (\w+);/\1 \2/;
    $c3 =~ s/private (\w+) (\w+);/\1 \2/;

    push(@un , $c1);
    push(@deux , $c2);
    push(@trois , $c3);
}

foreach (@un) {
    print $_;
}

print "\n";

foreach (@deux) {
    $_ =~ s/\s*(.+) (.+)/\1 get\2() {\nreturn this.\2;\n}\n/;
    print $_;
}

foreach (@trois) {
    $_ =~ s/\s*(.+) (.+)/void set\2(\1 \2) {\nthis.\2 = \2;\n}\n/;
    print $_;
}
