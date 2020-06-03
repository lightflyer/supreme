package tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Similarity {

    private Logger logger = LoggerFactory.getLogger(Similarity.class);

    public static boolean  is(String aa,String bb,double value) {
        if(aa==null||bb==null)
            return false;
        aa=filter(aa);
        bb=filter(bb);
        double cc=aa.length(), ee=bb.length();
        double ccc=getCommonStrLength(aa, bb)/((cc+ee)/2);
        if(ccc>value)
            return true;
        else
            return false;
    }

    public static double  getV(String aa,String bb) {
        if(aa==null||bb==null)
            return 0;
        System.out.println(bb);
        aa=filter(aa);
        bb=filter(bb);
        double cc=aa.length(), ee=bb.length();
        double ccc=getCommonStrLength(aa, bb)/((cc+ee)/2);
        return ccc;
    }

    public static  Map<String,String> maxSimilarity( List<Map> sizesList,String  databaseInputSize){
        try{
            if(databaseInputSize==null){
                databaseInputSize="";
            }
            String list[]=null;
            databaseInputSize=databaseInputSize.replace("，",",");
            if(databaseInputSize.contains(",")){
                list=databaseInputSize.split(",");
            }else{
                list =new String[1];
                list[0]=databaseInputSize;
            }
            Map<String,String> map=new HashMap <>();
            double max=-1;
            for(Map sizesData:sizesList) {
                for(String bb:list){
                    String sizeName = sizesData.get("name").toString();
                    bb=filter(bb);
                    sizeName=filter(sizeName);
                    double cc=sizeName.length(), ee=bb.length();
                    double ccc=getCommonStrLength(sizeName, bb)/(((cc*1.01+ee*1.01)/2));
                    if(ccc>max)
                    {
                        max=ccc;
                        map=sizesData;
                    }
                }
            }
            if(map.size()==0)
                return null;
            return  map;
        }catch (Throwable e){
            return null;
        }
    }

    public static List<Map> test(List<String> aa){
        List<Map> sizesList=new ArrayList<Map>();
        for(String a:aa){
            Map<String,String> mp1=new HashMap<>();
            mp1.put("name",a);
            sizesList.add(mp1);
        }
        return sizesList;
    }

    public static int index(List<String> list,String name){
        name=maxSimilarity(test(list),name).get("name");
        int index =0;
        for(;index<list.size();index++)
        {
            if(list.get(index).equals(name)){
                return index;
            }
        }
        return -1;
    }

    public static String getMaxSimilarity(List<String> list,String keywords){
        return maxSimilarity(test(list),keywords).get("name");
    }

    public static void main(String[] args) {
        List<String> test=new LinkedList<>();
        test.add("AIR-FLEX T-SHIRT WHITE");
        test.add("LEATHER ZIP WALLET GREY");
        test.add("P SPORT JOGGER NAVY");
        test.add("DEFLECTO FLAT SACK BLACK REFLECTIVE");
        test.add("SOFAR POCKET T-SHIRT SOFT BLUE");
        test.add("SAFE! 8.375");
        test.add("P 6-PANEL PINK");
        test.add("DEFLECTO FLIP STASH SILVER REFLECTIVE");
        test.add("UTILITY IRIDESCENT PANT BLACK");
        test.add("SOFAR POCKET T-SHIRT OATMEAL MARL");
        test.add("LOVE DOVE KNIT PURPLE");
        test.add("SOFAR POCKET T-SHIRT WHITE");
        test.add("TRIPPER RIB CREW BLACK");
        test.add("LINER LONGSLEEVE POLO RED");
        test.add("DOUBLE BUBBLE T-SHIRT BLACK");
        test.add("PALACE JAPAN MAGAZINE");
        test.add("TEX T-SHIRT BLACK / GREY");
        test.add("SAFE BADGE");
        test.add("SHELL SHORTS RED");
        test.add("VENT SHELL RUNNER BLACK");
        test.add("GLOBULAR T-SHIRT BLUE");
        test.add("F-LOCK CREW GREY");
        test.add("SMOCK SHELL HAT NAVY");
        test.add("METAL SHELL 6-PANEL FLURO");
        test.add("SOFAR HOOD BLACK");
        test.add("P SPORT HOOD BLACK");
        test.add("FLAGGO ZIP HOOD WHITE / GREEN / RED");
        test.add("POCKET STRIPE CREW NAVY / RED");
        test.add("P CLIP WEBBER BELT GREY");
        test.add("JUMBOTRONIC KNIT DARK NAVY");
        test.add("P 6-PANEL BLACK");
        test.add("UTILITY IRIDESCENT JACKET + VEST BLACK");
        test.add("MAXIMUM BEANIE BLACK");
        test.add("REVERSIBLE OVERLAY BOMBER ORANGE");
        test.add("PWLWCE CORD 6-PANEL YELLOW");
        test.add("P BEBE LONGSLEEVE GREY MARL");
        test.add("PALACE JEAN STONEWASH");
        test.add("P SPORT HOOD GREY MARL");
        test.add("FLASH SHELL P 6-PANEL NAVY");
        test.add("DEFLECTO SHOT 2 BAG SILVER REFLECTIVE");
        test.add("TRI-FADE HOOD GREY");
        test.add("SOFAR JOGGER WHITE");
        test.add("SOFAR POCKET LONGSLEEVE WHITE");
        test.add("PJ BOX T-SHIRT BLACK");
        test.add("TRI-FADE HOOD ORANGE");
        test.add("TOP OFF SHELL 6-PANEL BLUE");
        test.add("PALACE GATED COMMUNITY CREW BLACK");
        test.add("BASICALLY A SOCK BLACK");
        test.add("SEALER SHELL BOTTOMS NAVY");
        test.add("Q-ZIP HOOD GREEN");
        test.add("NEIN CUFF BEANIE JAMAICA");
        test.add("UTILITY SHELL BUCKET HAT OLIVE");
        test.add("SOFAR POCKET T-SHIRT ORANGE");
        test.add("STRAP SHELL 6-PANEL BLUE");
        test.add("P-FLEX HOOD WHITE");
        test.add("PRO TOOL HOOD BLUE");
        test.add("POCKET STRIPE CREW BLACK / ORANGE");
        test.add("PSB SHELL TOP NAVY");
        test.add("MAXIMUM BEANIE TEAL");
        test.add("SOFAR JOGGER GREY MARL");
        test.add("SOFAR JOGGER OATMEAL MARL");
        test.add("JUMBO DROP CREW BLACK");
        test.add("LONDINIUM T-SHIRT YELLOW");
        test.add("P-FLEX HOOD BLUE");
        test.add("HARDWARE T-SHIRT BLACK");
        test.add("REVERSIBLE OVERLAY BOMBER NAVY");
        test.add("SOFAR JOGGER NAVY");
        test.add("JUMBO DROP CREW BLUE");
        test.add("Q-ZIP HOOD WHITE");
        test.add("FLAGGO JOGGER WHITE / GREEN / RED");
        test.add("LONDINIUM T-SHIRT WHITE");
        test.add("RIPE BEANIE WHITE");
        test.add("HARDWARE T-SHIRT WHITE");
        test.add("METAL SHELL 6-PANEL WHITE");
        test.add("GLOBULAR T-SHIRT WHITE");
        test.add("CHEWY PRO S15 8.375");
        test.add("TONER BEANIE WHITE");
        test.add("REACTO P 6-PANEL RED");
        test.add("P-FLEX HOOD BLACK");
        test.add("REACTO JACKET HYPER BLACK");
        test.add("NUFF NUFF HOOD BLACK");
        test.add("LEATHER CARD HOLDER PINK");
        test.add("TWILL 1/4 HOOD GREY MARL");
        test.add("UTILITY IRIDESCENT JACKET + VEST GREY");
        test.add("FLAGGO JOGGER RED / WHITE / BLUE");
        test.add("TWILL 1/4 HOOD NAVY");
        test.add("TONER BEANIE PINK");
        test.add("P-CLIP 6-PANEL NAVY");
        test.add("TEX T-SHIRT AQUA / BLUE");
        test.add("CORD PANT YELLOW");
        test.add("Q-ZIP HOOD NAVY");
        test.add("SAFE! 8.1");
        test.add("TEAM PANEL JEAN WHITE / NAVY");
        test.add("MAXIMUM SHELL RUNNER GREY");
        test.add("P TIP SOCK BLACK");
        test.add("LONDINIUM T-SHIRT BLUE");
        test.add("GLOBULAR T-SHIRT YELLOW");
        test.add("P RACER TOP WHITE / BLACK");
        test.add("MAXIMUM SHELL RUNNER BLACK");
        test.add("FLASH SHELL P 6-PANEL BLACK");
        test.add("SOFAR POCKET T-SHIRT BLACK");
        test.add("PWLWCE T-SHIRT YELLOW");
        test.add("SAFE! 8.5");
        test.add("SOFAR POCKET T-SHIRT NAVY");
        test.add("PSB SHELL BOTTOMS GREY");
        test.add("COMBINER HOOD WHITE");
        test.add("POCKET STRIPE CREW WHITE / PURPLE");
        test.add("SOFAR HOOD BROWN");
        test.add("VENT SHELL RUNNER WHITE");
        test.add("P TIP SOCK GREY MARL");
        test.add("LUMBER YAK SHIRT RED");
        test.add("P-FLEX T-SHIRT GREY MARL");
        test.add("DUO JACKET GREEN");
        test.add("SAFE T HOOD WHITE");
        test.add("SHELL SHORTS BLUE");
        test.add("SEALER SHELL TOP GREEN");
        test.add("P-FLEX T-SHIRT BLUE");
        test.add("SIDE T-SHIRT WHITE");
        test.add("Q-ZIP HOOD BURGUNDY");
        test.add("PJ BOX T-SHIRT WHITE");
        test.add("P 6-PANEL BLUE");
        test.add("PALACE BAG FOR LIFE LARGE");
        test.add("TEAM WHEEL 54MM NEON GREEN");
        test.add("P TIP SOCK NAVY");
        test.add("PRO TOOL HOOD WHITE");
        test.add("LUMBER YAK SHIRT BLACK");
        test.add("PA-1 JACKET SILVER");
        test.add("GLOBULAR T-SHIRT NAVY");
        test.add("SOFAR POCKET LONGSLEEVE ORANGE");
        test.add("DEFLECTO 4-WAY BLACK REFLECTIVE");
        test.add("LINER LONGSLEEVE POLO BLUE");
        test.add("PWLWCE T-SHIRT BLUE");
        test.add("P 6-PANEL PURPLE");
        test.add("WAFFLER STRIPE CREW WHITE");
        test.add("TEAM JACKET WHITE / NAVY");
        test.add("DEFLECTO FLAT SACK SILVER REFLECTIVE");
        test.add("VENT SHELL RUNNER BLUE");
        test.add("NUGGET T-SHIRT YELLOW");
        test.add("COMBINER HOOD NAVY");
        test.add("PA-1 JACKET PETROL");
        test.add("PERTEX QUANTUM SHELL 6-PANEL BLACK");
        test.add("LEATHER BILLFOLD WALLET PINK");
        test.add("PA-1 JACKET OLIVE");
        test.add("SOFAR HOOD OATMEAL MARL");
        test.add("REACTO JACKET HYPER RED");
        test.add("P SPORT JOGGER BLACK");
        test.add("ZIG ZAG CREW BLACK / WHITE STRIPES");
        test.add("NEIN CUFF BEANIE UK");
        test.add("TEAM WHEEL 52MM GOLD");
        test.add("LEATHER ZIP WALLET PINK");
        test.add("SOFAR HOOD WHITE");
        test.add("PSB SHELL TOP GREY");
        test.add("CORD PANT LIGHT BLUE");
        test.add("PALACE GATED COMMUNITY CREW GREY MARL");
        test.add("JUMBO DROP CREW WHITE");
        test.add("PALACE GATED COMMUNITY CREW GREEN");
        test.add("PWLWCE BEANIE BLACK");
        test.add("PWLWCE T-SHIRT WHITE");
        test.add("PAL ACE SOCK BLACK");
        test.add("P 6-PANEL RUST");
        test.add("P SPORT HOOD NAVY");
        test.add("COMBINER SHORT WHITE");
        test.add("LINER LONGSLEEVE POLO BLACK");
        test.add("Q-ZIP HOOD VULCAN");
        test.add("F-LOCK PANEL CREW SAND");
        test.add("J T-SHIRT BLUE");
        test.add("SOFAR JOGGER BLACK");
        test.add("TRI-FADE HOOD TEAL");
        test.add("ZIPPED POLO ORANGE");
        test.add("PALACE JEAN BLACK STONEWASH");
        test.add("CLARKE PRO S15 8.25");
        test.add("DEFLECTO BUN BAG BLACK REFLECTIVE");
        test.add("PWLWCE CREW RED");
        test.add("NUFF NUFF T-SHIRT WHITE");
        test.add("TEAM WHEEL 51MM SILVER");
        test.add("ACROPALACE T-SHIRT BLUE");
        test.add("TREKNO FLEECE INTER GREENS");
        test.add("SOFAR POCKET LONGSLEEVE OATMEAL MARL");
        test.add("MATRIX SHIRT BLACK / GREEN");
        test.add("FLAGGIN T-SHIRT BLACK");
        test.add("AIR-FLEX T-SHIRT NAVY");
        test.add("P BEBE LONGSLEEVE BLACK");
        test.add("DUO JACKET GREY");
        test.add("BATTON-BERG KNIT BLACK / WHITE");
        test.add("SAFE T T-SHIRT GREY MARL");
        test.add("PERTEX QUANTUM JACKET OLIVE");
        test.add("PRO TOOL T-SHIRT WHITE");
        test.add("SHELL BOTTOMS BLACK");
        test.add("PWLWCE CORD 6-PANEL GREEN");
        test.add("TREKNO FLEECE INTER BLUES");
        test.add("BATTON-BERG KNIT ORANGE / BLUE");
        test.add("UTILITY IRIDESCENT PANT OLIVE");
        test.add("HARDWARE T-SHIRT BLUE");
        test.add("COMBINER SHORT NAVY");
        test.add("P-LINE RACER T-SHIRT WHITE");
        test.add("PRO TOOL T-SHIRT BLUE");
        test.add("NEIN CUFF BEANIE ITALY");
        test.add("PWLWCE SOCK BLACK");
        test.add("P-FLEX T-SHIRT WHITE");
        test.add("DOUBLE BUBBLE T-SHIRT NAVY");
        test.add("PALACE POUCH BLACK / WHITE");
        test.add("ACROPALACE T-SHIRT WHITE");
        test.add("FLAGGIN T-SHIRT RED");
        test.add("CORD PANT NAVY");
        test.add("SAFE T T-SHIRT WHITE");
        test.add("D-RIB BEANIE BLACK");
        test.add("FLAGGO JOGGER BLACK / WHITE / BLUE");
        test.add("COMBINER HOOD BLACK");
        test.add("TOP OFF SHELL BUCKET BLUE");
        test.add("PWLWCE CREW BLUE");
        test.add("PERTEX P-SPORT 1/2 ZIP THINSULATE BLACK");
        test.add("SOFAR POCKET T-SHIRT OLIVE");
        test.add("RIPE BEANIE YELLOW");
        test.add("RIPE BEANIE GREY MARL");
        test.add("P-BELA LONGSLEEVE GREEN");
        test.add("F-LOCK PANEL CREW VULCAN");
        test.add("SMOCK SHELL HAT BLACK");
        test.add("PWLWCE CORD 6-PANEL BLACK");
        test.add("DEFLECTO 4-WAY SILVER REFLECTIVE");
        test.add("STRAP SHELL 6-PANEL WHITE");
        test.add("SEALER SHELL BOTTOMS BLACK");
        test.add("DEFLECTO BUN BAG SILVER REFLECTIVE");
        test.add("ACROPALACE T-SHIRT BLACK");
        test.add("P-BELA LONGSLEEVE WHITE");
        test.add("SAFE T HOOD BLACK");
        test.add("PERTEX QUANTUM JACKET YELLOW");
        test.add("SOFAR POCKET T-SHIRT BROWN");
        test.add("TRIPPER RIB CREW PURPLE");
        test.add("SEALER SHELL TOP BLACK");
        test.add("P SPORT JOGGER GREY MARL");
        test.add("HARDWARE ZIP HOOD NAVY");
        test.add("PRO TOOL HOOD BLACK");
        test.add("F-LOCK CREW PURPLE");
        test.add("TONER BEANIE BLACK");
        test.add("P TIP SOCK WHITE");
        test.add("MAXIMUM SHELL RUNNER WHITE");
        test.add("RORY PRO S15 8.06");
        test.add("PWLWCE SOCK WHITE");
        test.add("PERTEX P-SPORT 1/2 ZIP THINSULATE NAVY");
        test.add("THE PALACE Alasdair Mclellan");
        test.add("F-LOCK PANEL CREW PETROL");
        test.add("S-RUNNER 2 SHELL HAT WHITE / PEARL");
        test.add("AIR-FLEX T-SHIRT ORANGE");
        test.add("P-CLIP 6-PANEL RED");
        test.add("PRO TOOL T-SHIRT NAVY");
        test.add("P RACER TOP BLACK");
        test.add("J T-SHIRT ORANGE");
        test.add("P BEBE LONGSLEEVE WHITE");
        test.add("SHELL BOTTOMS GREY");
        test.add("P CLIP WEBBER BELT BLACK");
        test.add("NUGGET T-SHIRT BLUE");
        test.add("PJ BOX T-SHIRT BLUE");
        test.add("SOFAR JOGGER BROWN");
        test.add("NUGGET T-SHIRT RED");
        test.add("LUCAS PUIG S15 PRO 8.2");
        test.add("ZIG ZAG CREW NAVY / WHITE STRIPES");
        test.add("LONDINIUM T-SHIRT GREY MARL");
        test.add("MAXIMUM BEANIE BLUE");
        test.add("P-BELA LONGSLEEVE GREY MARL");
        test.add("P-FLEX HOOD GREY MARL");
        test.add("ZIPPED POLO BLACK");
        test.add("TREKNO FLEECE HAT INTER BLUES");
        test.add("PALLAS 8.6");
        test.add("STRAP SHELL 6-PANEL GREY");
        test.add("PAL ACE SOCK WHITE");
        test.add("PWLWCE T-SHIRT BLACK");
        test.add("UTILITY SHELL BUCKET HAT BLACK");
        test.add("SHELL SHORTS ORANGE");
        test.add("SOFAR HOOD ORANGE");
        test.add("SOFAR POCKET LONGSLEEVE OLIVE");
        test.add("Q-ZIP HOOD ORANGE");
        test.add("STICKER PACK SPRING");
        test.add("GLOBULAR T-SHIRT BLACK");
        test.add("HARDWARE ZIP HOOD BLACK");
        test.add("PALACE GATED COMMUNITY CREW NAVY");
        test.add("SOFAR HOOD GREY MARL");
        test.add("PWLWCE BEANIE YELLOW");
        test.add("SEALER SHELL TOP NAVY");
        test.add("ACROPALACE T-SHIRT GREY MARL");
        test.add("P-LINE RACER T-SHIRT BLACK");
        test.add("PRO TOOL T-SHIRT BLACK");
        test.add("NEIN CUFF BEANIE GERMANY");
        test.add("MAXIMUM BEANIE WHITE");
        test.add("LEATHER BILLFOLD WALLET GREY");
        test.add("WAFFLER STRIPE CREW SKY");
        test.add("PERTEX QUANTUM SHELL 6-PANEL OLIVE");
        test.add("P RACER TOP NAVY");
        test.add("PSB SHELL TOP BLACK");
        test.add("ZIPPED POLO NAVY");
        test.add("BASICALLY A SOCK WHITE");
        test.add("SAFE T T-SHIRT BLUE");
        test.add("TOP OFF SHELL 6-PANEL GREEN");
        test.add("TEAM WHEEL 53MM NEON ORANGE");
        test.add("PWLWCE 8.1");
        test.add("NUFF NUFF T-SHIRT PURPLE");
        test.add("DEFLECTO TUBE SILVER REFLECTIVE");
        test.add("D-RIB BEANIE ORANGE");
        test.add("TODD PRO S15 7.75");
        test.add("FLAGGO ZIP HOOD RED / WHITE / BLUE");
        test.add("REACTO P 6-PANEL BLUE");
        test.add("UTILITY IRIDESCENT JACKET + VEST OLIVE");
        test.add("SLIDER PENDANT");
        test.add("TEX T-SHIRT PURPLE / NAVY");
        test.add("PWLWCE CREW YELLOW");
        test.add("TOP OFF SHELL 6-PANEL BLACK");
        test.add("F-LOCK CREW NAVY");
        test.add("PWLWCE T-SHIRT GREY MARL");
        test.add("P-CLIP 6-PANEL BLACK");
        test.add("TOP OFF SHELL BUCKET BLACK");
        test.add("TWILL 1/4 HOOD BLACK");
        test.add("SHELL PAINTER PANT OLIVE");
        test.add("PSB SHELL BOTTOMS NAVY");
        test.add("DOUBLE BUBBLE T-SHIRT WHITE");
        test.add("PJ BOX T-SHIRT GREEN");
        test.add("SHELL SHORTS BLACK");
        test.add("P-LINE RACER T-SHIRT NAVY");
        test.add("P-FLEX T-SHIRT BLACK");
        test.add("SAFE T HOOD YELLOW");
        test.add("FLAGGIN T-SHIRT ORANGE");
        test.add("NUFF NUFF HOOD WHITE");
        test.add("NUGGET T-SHIRT WHITE");
        test.add("SAFE T T-SHIRT BLACK");
        test.add("PALACE BOXERS BLACK / NAVY / WHITE");
        test.add("DUO JACKET BLUE");
        test.add("DEFLECTO FLIP STASH BLACK REFLECTIVE");
        test.add("P 6-PANEL WHITE");
        test.add("SOFAR POCKET LONGSLEEVE NAVY");
        test.add("D-RIB BEANIE GREEN");
        test.add("GLOBULAR T-SHIRT GREY MARL");
        test.add("P-BELA LONGSLEEVE BLACK");
        test.add("S-RUNNER 2 SHELL HAT GREY / PEARL");
        test.add("PSB SHELL BOTTOMS BLACK");
        test.add("HARDWARE ZIP HOOD GREY MARL");
        test.add("COMBINER SHORT BLACK");
        test.add("SHELL PAINTER PANT NAVY");
        test.add("SAFE T HOOD GREY MARL");
        test.add("REVERSIBLE OVERLAY BOMBER OLIVE");
        test.add("SEALER SHELL BOTTOMS GREEN");
        test.add("LUMBER YAK SHIRT NAVY / GREEN CHECK");
        test.add("ACROPALACE CREW BLUE");
        test.add("PWLWCE BEANIE WHITE");
        test.add("DEFLECTO SLING SACK SILVER REFLECTIVE");
        test.add("METAL SHELL 6-PANEL BLACK");
        test.add("FLAGGIN T-SHIRT NAVY");
        test.add("PERTEX QUANTUM JACKET BLACK");
        test.add("SOFAR HOOD SOFT BLUE");
        test.add("TEMP TATTOOS");
        test.add("TRIPPER RIB CREW BLUE");
        test.add("NUFF NUFF T-SHIRT BLACK");
        test.add("FLAGGO ZIP HOOD BLACK / WHITE / BLUE");
        test.add("JUMBOTRONIC KNIT CREAM");
        test.add("DEFLECTO SHOT 2 BAG BLACK REFLECTIVE");
        test.add("STRAP SHELL 6-PANEL RED");
        test.add("REACTO P 6-PANEL BLACK");
        test.add("S-RUNNER 2 SHELL HAT BLACK / PEARL");
        test.add("PERTEX P-SPORT 1/2 ZIP THINSULATE GREY");
        test.add("TONER BEANIE TEAL");
        test.add("SHELL BOTTOMS NAVY");
        test.add("PRO TOOL T-SHIRT GREY MARL");
        test.add("NUFF NUFF HOOD PURPLE");
        test.add("TREKNO FLEECE HAT INTER GREENS");
        test.add("LEATHER CARD HOLDER GREY");
        test.add("D-RIB BEANIE BLUE");
        test.add("WAFFLER STRIPE CREW NAVY");
        test.add("PJ BOX T-SHIRT NAVY");
        test.add("DEFLECTO TUBE BLACK REFLECTIVE");
        test.add("FAIRFAX PRO S15 8.06");
        test.add("UTILITY IRIDESCENT PANT GREY");
        test.add("SB 8.25");
        test.add("TOP OFF SHELL BUCKET GREEN");
        test.add("SOFAR POCKET LONGSLEEVE BLACK");
        test.add("SMOCK SHELL HAT GREY");
        test.add("SIDE T-SHIRT NAVY");
        test.add("PWLWCE T-SHIRT GREEN");
        test.add("SOFAR HOOD NAVY");
        test.add("TRI-FADE HOOD WHITE");
        test.add("STRAP SHELL 6-PANEL BLACK");
        test.add("REACTO JACKET HYPER BLUE");
        test.add("PRO TOOL HOOD NAVY");
        test.add("DEFLECTO SLING SACK BLACK REFLECTIVE");
        test.add("SAFE T HOOD BLUE");
        test.add("MAXIMUM BEANIE ORANGE");
        test.add("PWLWCE CORD 6-PANEL NAVY");
        test.add("TEX T-SHIRT ORANGE / PURPLE");
        test.add("NUGGET T-SHIRT GREY MARL");
        test.add("ACROPALACE CREW BLACK");
        test.add("PERTEX QUANTUM SHELL 6-PANEL YELLOW");
        test.add("BRADY PRO S15 8");
        test.add("LOVE DOVE KNIT GREY");
        test.add("FLASH SHELL P 6-PANEL TEAL");
        test.add("LONDINIUM T-SHIRT BLACK");
        test.add("PWLWCE 8");
        test.add("PWLWCE CREW GREEN");
        test.add("SAFE T T-SHIRT GREEN");
        test.add("SIDE T-SHIRT BLACK");
        test.add("RIPE BEANIE NAVY");
        test.add("ACROPALACE T-SHIRT GREEN");

        System.out.println(maxSimilarity(test(test),"SAFE T HOOD WHITE"));
//        System.out.println(Similarity.is(" ScriptSideline﻿Sleeve ", "Sle﻿eve S﻿cr﻿ip﻿t S﻿i﻿d﻿e﻿lin﻿e﻿ ﻿Ja﻿cket"));;
//        System.out.println(Similarity.is(" ScriptSideline﻿", "Sle﻿eve S﻿cr﻿ip﻿t S﻿i﻿d﻿e﻿lin﻿e﻿ ﻿Ja﻿cket"));;
//        System.out.println(Similarity.is(" Sleeve\uFEFF \uFEFFScrip\uFEFFt\uFEFF Sideli\uFEFFne\uFEFF Jacke\uFEFFt", "Sideline Jacket"));;
//        System.out.println(Similarity.is(" Sle﻿eve S﻿cr﻿ip﻿t S﻿i﻿d﻿e﻿lin﻿e", "Sle﻿eve S﻿cr﻿ip﻿t S﻿i﻿d﻿e﻿lin﻿e﻿ ﻿Ja﻿cket"));;
//        System.out.println(Similarity.is(" Zi\uFEFFp Up\uFEFF W\uFEFFork\uFEFF S\uFEFFhirt\uFEFF", "Work Shirt"));;
//        System.out.println(Similarity.is(" Shirt", "Work Shirt"));;
//        System.out.println(Similarity.is(" Supre\uFEFFm\uFEFFe®/Wilso\uFEFFn® \uFEFFT\uFEFFennis Bal\uFEFFl\uFEFFs ", "Supreme®/Wilson® Tennis Balls"));;
//        System.out.println(Similarity.is("Supreme®/Hanes® Tagless Tees (3 Pack)", "Sup\uFEFFreme®/Han\uFEFFes®\uFEFF T\uFEFFa\uFEFFgle\uFEFFss\uFEFF Tees (3 Pack\uFEFF)"));;
    }

    /***
     * 根据名字匹配对应的商品
     */
//    private String match(String content,String name,String regex) {
//        //这个规则是先匹配大块，就是包含了连接&名字
//        int index=Similarity.index(RegexParse.baseParseList(content,regex,2),name);
//        return RegexParse.baseParseList(content,regex,1).get(index);
//    }

    public static  String filter(String  in){
        if(in==null)
            return in;
        in= RegexParse.baseParseList(in,"[\\w]",0).toString();
        in=in.toUpperCase();
        in=in.replace(",","").replaceAll("\\s","").replaceAll("\\]","").replaceAll("\\[","");
        return in;
    }

    public static int getCommonStrLength(String str1, String str2) {
        str1 = str1.toLowerCase();
        str2 = str2.toLowerCase();
        int len1 = str1.length();
        int len2 = str2.length();
        String min = null;
        String max = null;
        String target = null;
        min = len1 <= len2 ? str1 : str2;
        max = len1 > len2 ? str1 : str2;
        //最外层：min子串的长度，从最大长度开始
        for (int i = min.length(); i >= 1; i--) {
            //遍历长度为i的min子串，从0开始
            for (int j = 0; j <= min.length() - i; j++) {
                target = min.substring(j, j + i);
                //遍历长度为i的max子串，判断是否与target子串相同，从0开始
                for (int k = 0; k <= max.length() - i; k++) {
                    if (max.substring(k,k + i).equals(target)) {
                        return i;
                    }
                }
            }
        }
        return 0;
    }
}
