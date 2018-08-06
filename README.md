## 発現データ（tab, gct format)のprocessiong
tab format
>[tab]sample1[tab]sample2[tab]sample3  
gene1[tab]1.0[tab]2.0[tab]3.0  
gene2[tab]4.0[tab]5.0[tab]6.0  
gene3[tab]7.0[tab]8.0[tab]9.0

1以下を1にする
```
perl  ExpCnUtility/perl/expression.pl　-f 1 exp.tab  > exp2.tab
```

log10にスケールを変換
```
perl  ExpCnUtility/perl/expression.pl  -l 10 exp.tab  > exp2.tab
```

rowごとに（つまり各遺伝子ごとに）分散１平均0に標準化
```
perl  ExpCnUtility/perl/expression.pl  -r exp.tab  > exp2.tab
```

columごと（つまり各サンプルごとに）平均100にそろえる
```
perl  ExpCnUtility/perl/expression.pl  -m 100  exp.tab  > exp2.tab
```

varianceの高い5000遺伝子にしぼる
```
perl  ExpCnUtility/perl/expression.pl  -v 5000  exp.tab  > exp2.tab
```


## コピー数データ（seg format)のprocessiong
seg format
https://software.broadinstitute.org/software/igv/SEG


長さ100000未満のsegmentを除く
```
perl  ExpCnUtility/perl/segment.pl  -S   100000  cn.seg  > cn2.seg
```

\>0.1を1　≦0.1 を0　
```
perl  ExpCnUtility/perl/segment.pl  -B 0.1    cn.seg  >  cn2.seg
```

<-0.1を1　≧-0.1 を0　
```
perl  ExpCnUtility/perl/segment.pl  -L -0.1    cn.seg  >  cn2.seg
```

\>0.1を1   <-0.1を-1　それ以外を0  
```
perl  ExpCnUtility/perl/segment.pl  -B 0.1 -L -0.1    cn.seg  >  cn2.seg
```

\> 90 percentile を1　≦ 90 percentile  を0　
```
perl  ExpCnUtility/perl/segment.pl  -G 0.9    cn.seg  >  cn2.seg
```

< 10 percentile を1　≧ 10 percentile を0　
```
perl  ExpCnUtility/perl/segment.pl  -K 0.1    cn.seg  >  cn2.seg
```

\> 90 percentile を1   < 10 percentile を-1　それ以外を0　
```
perl  ExpCnUtility/perl/segment.pl  -G 0.9 -K 0.1    cn.seg  >  cn2.seg
```

XY染色体を除く
```
perl  ExpCnUtility/perl/segment.pl  -r   cn.seg  > cn2.seg
```
segmentのギャップを埋める
```
perl  ExpCnUtility/ExpCnUtility/perl/segment.pl -f cn.seg >  cn.fld.seg
```


遺伝子ごとのコピー数をもとめる
```
perl  ExpCnUtility/perl/segment.pl  -M -b ExpCnUtility/data/symbol.hg19.bed  cn.fld.seg  >  cn.gene.tab
```

armごとのコピー数をもとめる
```
perl  ExpCnUtility/perl/segment.pl  -M -b   ExpCnUtility/data/arm.hg19.bed  cn.fld.seg  >  cn.arm.tab
```

約10000個のprobe数のmatrixを作る
```
perl  ExpCnUtility/perl/segment.pl  -M -n   10000  cn.fld.seg  >  cn.10000.tab
```
