package com.example.project.model;

public class ProductDescription {
    private String hz;
    private String vga;
    private String cpu;
    private String ssd;
    private String lcd;
    private String ram;

    public ProductDescription(String hz, String vga, String cpu, String ssd, String lcd, String ram) {
        this.hz = hz;
        this.vga = vga;
        this.cpu = cpu;
        this.ssd = ssd;
        this.lcd = lcd;
        this.ram = ram;
    }

    public ProductDescription() {
    }

    public String getHz() {
        return hz;
    }

    public void setHz(String hz) {
        this.hz = hz;
    }

    public String getVga() {
        return vga;
    }

    public void setVga(String vga) {
        this.vga = vga;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getSsd() {
        return ssd;
    }

    public void setSsd(String ssd) {
        this.ssd = ssd;
    }

    public String getLcd() {
        return lcd;
    }

    public void setLcd(String lcd) {
        this.lcd = lcd;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }
}
