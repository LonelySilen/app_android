package silen.app.bean;

import java.io.Serializable;

/*
 *
 *
 * 版 权 :@Copyright *****有限公司版权所有
 *
 * 作 者 :lonelysilen
 *
 * 版 本 :1.0
 *
 * 创建日期 :2016/11/10  10:21
 *
 * 描 述 :*****处理类
 *
 * 修订日期 :
 */

/**
 * Created by admin on 2016/10/25.
 */

public class Container implements Serializable {
    private int _id;
    private boolean state;
    private String taskmasterid;
    private String yundanid;
    private String yundanhao;
    private String fazhan;
    private String daozhan;
    private String zhuanyongxianmingcheng;
    private String tuoyuanren;
    private String shouhuoren;
    private String xiangshu;
    private String pinming;
    private String xiangxing;
    private String xianghao;
    private String zhongliang;
    private String riqi;
    private String chezhong;
    private String chexing;

    public Container() {
    }

    public Container(int _id, boolean state, String taskmasterid, String yundanid, String yundanhao,
                     String fazhan, String daozhan, String zhuanyongxianmingcheng,
                     String tuoyuanren, String shouhuoren, String xiangshu, String pinming,
                     String xiangxing, String xianghao, String zhongliang, String riqi,
                     String chezhong, String chexing) {
        this._id = _id;
        this.state = state;
        this.taskmasterid = taskmasterid;
        this.yundanid = yundanid;
        this.yundanhao = yundanhao;
        this.fazhan = fazhan;
        this.daozhan = daozhan;
        this.zhuanyongxianmingcheng = zhuanyongxianmingcheng;
        this.tuoyuanren = tuoyuanren;
        this.shouhuoren = shouhuoren;
        this.xiangshu = xiangshu;
        this.pinming = pinming;
        this.xiangxing = xiangxing;
        this.xianghao = xianghao;
        this.zhongliang = zhongliang;
        this.riqi = riqi;
        this.chezhong = chezhong;
        this.chexing = chexing;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getTaskmasterid() {
        return taskmasterid;
    }

    public void setTaskmasterid(String taskmasterid) {
        this.taskmasterid = taskmasterid;
    }

    public String getYundanid() {
        return yundanid;
    }

    public void setYundanid(String yundanid) {
        this.yundanid = yundanid;
    }

    public String getYundanhao() {
        return yundanhao;
    }

    public void setYundanhao(String yundanhao) {
        this.yundanhao = yundanhao;
    }

    public String getFazhan() {
        return fazhan;
    }

    public void setFazhan(String fazhan) {
        this.fazhan = fazhan;
    }

    public String getDaozhan() {
        return daozhan;
    }

    public void setDaozhan(String daozhan) {
        this.daozhan = daozhan;
    }

    public String getZhuanyongxianmingcheng() {
        return zhuanyongxianmingcheng;
    }

    public void setZhuanyongxianmingcheng(String zhuanyongxianmingcheng) {
        this.zhuanyongxianmingcheng = zhuanyongxianmingcheng;
    }

    public String getTuoyuanren() {
        return tuoyuanren;
    }

    public void setTuoyuanren(String tuoyuanren) {
        this.tuoyuanren = tuoyuanren;
    }

    public String getShouhuoren() {
        return shouhuoren;
    }

    public void setShouhuoren(String shouhuoren) {
        this.shouhuoren = shouhuoren;
    }

    public String getXiangshu() {
        return xiangshu;
    }

    public void setXiangshu(String xiangshu) {
        this.xiangshu = xiangshu;
    }

    public String getPinming() {
        return pinming;
    }

    public void setPinming(String pinming) {
        this.pinming = pinming;
    }

    public String getXiangxing() {
        return xiangxing;
    }

    public void setXiangxing(String xiangxing) {
        this.xiangxing = xiangxing;
    }

    public String getXianghao() {
        return xianghao;
    }

    public void setXianghao(String xianghao) {
        this.xianghao = xianghao;
    }

    public String getZhongliang() {
        return zhongliang;
    }

    public void setZhongliang(String zhongliang) {
        this.zhongliang = zhongliang;
    }

    public String getRiqi() {
        return riqi;
    }

    public void setRiqi(String riqi) {
        this.riqi = riqi;
    }

    public String getChezhong() {
        return chezhong;
    }

    public void setChezhong(String chezhong) {
        this.chezhong = chezhong;
    }

    public String getChexing() {
        return chexing;
    }

    public void setChexing(String chexing) {
        this.chexing = chexing;
    }
}