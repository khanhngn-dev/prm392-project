package utils.https.types.response;

import com.example.project.model.ProductDescription;

public class ProductDescriptionResponse {
    private String hl_hz;
    private String hl_vga;
    private String hl_cpu;
    private String hl_ssd;
    private String hl_lcd;
    private String hl_ram;

    public ProductDescription toModel() {
        return new ProductDescription(hl_hz, hl_vga, hl_cpu, hl_ssd, hl_lcd, hl_ram);
    }

    public ProductDescriptionResponse(String hl_hz, String hl_vga, String hl_cpu, String hl_ssd, String hl_lcd, String hl_ram) {
        this.hl_hz = hl_hz;
        this.hl_vga = hl_vga;
        this.hl_cpu = hl_cpu;
        this.hl_ssd = hl_ssd;
        this.hl_lcd = hl_lcd;
        this.hl_ram = hl_ram;
    }

    public ProductDescriptionResponse() {
    }

    public String getHl_hz() {
        return hl_hz;
    }

    public void setHl_hz(String hl_hz) {
        this.hl_hz = hl_hz;
    }

    public String getHl_vga() {
        return hl_vga;
    }

    public void setHl_vga(String hl_vga) {
        this.hl_vga = hl_vga;
    }

    public String getHl_cpu() {
        return hl_cpu;
    }

    public void setHl_cpu(String hl_cpu) {
        this.hl_cpu = hl_cpu;
    }

    public String getHl_ssd() {
        return hl_ssd;
    }

    public void setHl_ssd(String hl_ssd) {
        this.hl_ssd = hl_ssd;
    }

    public String getHl_lcd() {
        return hl_lcd;
    }

    public void setHl_lcd(String hl_lcd) {
        this.hl_lcd = hl_lcd;
    }

    public String getHl_ram() {
        return hl_ram;
    }

    public void setHl_ram(String hl_ram) {
        this.hl_ram = hl_ram;
    }
}
