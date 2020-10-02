package site.liangbai.lrobot.contact.poke;

import site.liangbai.lrobot.contact.LR;

import java.util.Arrays;
import java.util.List;

public class Poke {
    /** 戳一戳 */
    public static String Poke = LR.poke("戳一戳", 1, -1);

    /** 比心 */
    public static String ShowLove = LR.poke("比心", 2, -1);

    /** 点赞  */
    public static String Like = LR.poke("点赞", 3, -1);

    /** 心碎 */
    public static String Heartbroken = LR.poke("心碎", 4, -1);

    /** 666 */
    public static String SixSixSix = LR.poke("666", 5, -1);

    /** 放大招 */
    public static String FangDaZhao = LR.poke("放大招", 6, -1);

    /** 宝贝球 (SVIP) */
    public static String BaoBeiQiu = LR.poke("宝贝球", 126, 2011);

    /** 玫瑰花 (SVIP) */
    public static String Rose = LR.poke("玫瑰花", 126, 2007);

    /** 召唤术 (SVIP) */
    public static String ZhaoHuanShu = LR.poke("召唤术", 126, 2006);

    /** 让你皮 (SVIP) */
    public static String RangNiPi = LR.poke("让你皮", 126, 2009);

    /** 结印 (SVIP) */
    public static String JieYin = LR.poke("结印", 126, 2005);

    /** 手雷 (SVIP) */
    public static String ShouLei = LR.poke("手雷", 126, 2004);

    /** 勾引 */
    public static String GouYin = LR.poke("勾引", 126, 2003);

    /** 抓一下 (SVIP) */
    public static String ZhuaYiXia = LR.poke("抓一下", 126, 2001);

    /** 碎屏 (SVIP) */
    public static String SuiPing = LR.poke("碎屏", 126, 2002);

    /** 敲门 (SVIP) */
    public static String QiaoMen = LR.poke("敲门", 126, 2002);

    public static List<String> values = Arrays.asList(Poke, ShowLove, Like, Heartbroken, SixSixSix,
            FangDaZhao, BaoBeiQiu, Rose, ZhaoHuanShu, RangNiPi,
            JieYin, ShouLei, GouYin, ZhuaYiXia, SuiPing);

}
