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
 * Created by admin on 2016/10/24.
 */

public class Task implements Serializable {
    private int _id;
    private boolean state;
    private String mendiaohao;
    private String mendiaomingcheng;
    private String fabushijian;
    private String renwunum;
    private String faburenid;
    private String faburenxingming;
    private String fabuflag;
    private String jieshourenid;
    private String jieshoushijian;
    private String dangqianzhuangtai;

    public Task() {
    }

    public Task(int _id, boolean state, String mendiaohao, String mendiaomingcheng, String fabushijian,
                String renwunum, String faburenid, String faburenxingming, String fabuflag,
                String jieshourenid, String jieshoushijian, String dangqianzhuangtai) {
        this._id = _id;
        this.state = state;
        this.mendiaohao = mendiaohao;
        this.mendiaomingcheng = mendiaomingcheng;
        this.fabushijian = fabushijian;
        this.renwunum = renwunum;
        this.faburenid = faburenid;
        this.faburenxingming = faburenxingming;
        this.fabuflag = fabuflag;
        this.jieshourenid = jieshourenid;
        this.jieshoushijian = jieshoushijian;
        this.dangqianzhuangtai = dangqianzhuangtai;
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
    public String getMendiaohao() {
        return mendiaohao;
    }

    public void setMendiaohao(String mendiaohao) {
        this.mendiaohao = mendiaohao;
    }

    public String getMendiaomingcheng() {
        return mendiaomingcheng;
    }

    public void setMendiaomingcheng(String mendiaomingcheng) {
        this.mendiaomingcheng = mendiaomingcheng;
    }

    public String getFabushijian() {
        return fabushijian;
    }

    public void setFabushijian(String fabushijian) {
        this.fabushijian = fabushijian;
    }

    public String getRenwunum() {
        return renwunum;
    }

    public void setRenwunum(String renwunum) {
        this.renwunum = renwunum;
    }

    public String getFaburenid() {
        return faburenid;
    }

    public void setFaburenid(String faburenid) {
        this.faburenid = faburenid;
    }

    public String getFaburenxingming() {
        return faburenxingming;
    }

    public void setFaburenxingming(String faburenxingming) {
        this.faburenxingming = faburenxingming;
    }

    public String getFabuflag() {
        return fabuflag;
    }

    public void setFabuflag(String fabuflag) {
        this.fabuflag = fabuflag;
    }

    public String getJieshourenid() {
        return jieshourenid;
    }

    public void setJieshourenid(String jieshourenid) {
        this.jieshourenid = jieshourenid;
    }

    public String getJieshoushijian() {
        return jieshoushijian;
    }

    public void setJieshoushijian(String jieshoushijian) {
        this.jieshoushijian = jieshoushijian;
    }

    public String getDangqianzhuangtai() {
        return dangqianzhuangtai;
    }

    public void setDangqianzhuangtai(String dangqianzhuangtai) {
        this.dangqianzhuangtai = dangqianzhuangtai;
    }
}

