package com.mobile.tiandy.asset.common.vo;

import java.io.Serializable;

/**
 * Created by 78326 on 2017.9.2.
 */
public class Asset implements Serializable{
    private Long id;//序号
    private String codeId;//资产编号
    private String jobId;//工号
    private String userName; //用户名
    private String type;//资产类型
    private String name;//资产名称
    private String model;//资产型号
    private String place;//存储地点
    private String floor;//存储楼层
    private String realPlace;//实际存储地点
    private String saver;//保管人
    private String realSaver;//实际保管人
    private int state;//状态（操作）
    private String stateText;//状态（操作）
    private String build;//本部
    private String center;//中心
    private String part;//部门
    private String leavePlace;//存放地点
    private String cpu;
    private String board;//主板
    private String memory;//内存
    private String disk;//硬盘
    private String box;//机箱
    private String videoCard;//显卡
    private String softDriver;//软驱
    private String hardDriver;//光驱
    private String other;//其他
    private String price;//单价
    private String count;//数量
    private String money;//金额
    private String cost;//每月花费
    private String time;//购入时间
    private String costIt;//购入时间
    private boolean isExpand;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getRealPlace() {
        return realPlace;
    }

    public void setRealPlace(String realPlace) {
        this.realPlace = realPlace;
    }

    public String getSaver() {
        return saver;
    }

    public void setSaver(String saver) {
        this.saver = saver;
    }

    public String getRealSaver() {
        return realSaver;
    }

    public void setRealSaver(String realSaver) {
        this.realSaver = realSaver;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getLeavePlace() {
        return leavePlace;
    }

    public void setLeavePlace(String leavePlace) {
        this.leavePlace = leavePlace;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getDisk() {
        return disk;
    }

    public void setDisk(String disk) {
        this.disk = disk;
    }

    public String getBox() {
        return box;
    }

    public void setBox(String box) {
        this.box = box;
    }

    public String getVideoCard() {
        return videoCard;
    }

    public void setVideoCard(String videoCard) {
        this.videoCard = videoCard;
    }

    public String getSoftDriver() {
        return softDriver;
    }

    public void setSoftDriver(String softDriver) {
        this.softDriver = softDriver;
    }

    public String getHardDriver() {
        return hardDriver;
    }

    public void setHardDriver(String hardDriver) {
        this.hardDriver = hardDriver;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCostIt() {
        return costIt;
    }

    public void setCostIt(String costIt) {
        this.costIt = costIt;
    }

    public String getStateText() {
        return stateText;
    }

    public void setStateText(String stateText) {
        this.stateText = stateText;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
