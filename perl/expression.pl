#!/usr/local/bin/perl 

use strict;
use warnings;
use FindBin;
use lib "$FindBin::Bin";

my $base  = $FindBin::Bin;
$base =~ s/\/[^\/]*$//;
chomp(my @lib = `ls  \'$base/java/lib\'`);
@lib = map {"$base/java/lib/$_"} @lib;
$ENV{CLASSPATH} = "$base/java/bin:".join(":",  @lib); 
my $javaHeap = 2048; 

my $command =  "java  -Xms${javaHeap}m -Xmx${javaHeap}m Expression ";
exit(system($command.join(" ", @ARGV)));
