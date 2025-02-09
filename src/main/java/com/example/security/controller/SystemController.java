package com.example.security.controller;

import com.example.security.service.SystemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Map;

@Controller
@RequestMapping("/admin/system")
public class SystemController {

    private final SystemService systemService;

    public SystemController(SystemService systemService) {
        this.systemService = systemService;
    }

    @GetMapping
    public String systemSettings(Model model) {
        model.addAttribute("settings", systemService.getAllSettings());
        model.addAttribute("systemInfo", systemService.getSystemInfo());
        return "admin/system/settings";
    }

    @PostMapping("/settings/update")
    public String updateSettings(@RequestParam Map<String, String> settings, 
                               RedirectAttributes redirectAttributes) {
        try {
            systemService.updateSettings(settings);
            redirectAttributes.addFlashAttribute("success", "系统设置更新成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "更新失败: " + e.getMessage());
        }
        return "redirect:/admin/system";
    }

    @GetMapping("/logs")
    public String viewLogs(Model model) {
        model.addAttribute("logs", systemService.getRecentLogs());
        return "admin/system/logs";
    }

    @PostMapping("/cache/clear")
    public String clearCache(RedirectAttributes redirectAttributes) {
        try {
            systemService.clearCache();
            redirectAttributes.addFlashAttribute("success", "缓存清理成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "缓存清理失败: " + e.getMessage());
        }
        return "redirect:/admin/system";
    }
} 