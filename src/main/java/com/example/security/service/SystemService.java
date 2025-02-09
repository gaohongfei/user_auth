package com.example.security.service;

import org.springframework.stereotype.Service;
import org.springframework.cache.CacheManager;
import org.springframework.core.env.Environment;
import java.util.*;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

@Service
public class SystemService {

    private final Environment env;
    private final CacheManager cacheManager;

    public SystemService(Environment env, CacheManager cacheManager) {
        this.env = env;
        this.cacheManager = cacheManager;
    }

    public Map<String, String> getAllSettings() {
        Map<String, String> settings = new HashMap<>();
        
        // 应用信息
        settings.put("应用名称", getPropertyOrDefault("spring.application.name", "未设置"));
        settings.put("服务端口", getPropertyOrDefault("server.port", "8080"));
        settings.put("当前环境", getPropertyOrDefault("spring.profiles.active", "default"));
        
        // 数据库配置
        settings.put("数据库URL", maskSensitiveInfo(getPropertyOrDefault("spring.datasource.url", "未设置")));
        settings.put("数据库用户", maskSensitiveInfo(getPropertyOrDefault("spring.datasource.username", "未设置")));
        
        // JPA配置
        settings.put("JPA显示SQL", getPropertyOrDefault("spring.jpa.show-sql", "false"));
        settings.put("数据库方言", getPropertyOrDefault("spring.jpa.database-platform", "未设置"));
        
        // 服务器配置
        settings.put("会话超时", getPropertyOrDefault("server.servlet.session.timeout", "30m"));
        settings.put("最大文件大小", getPropertyOrDefault("spring.servlet.multipart.max-file-size", "10MB"));
        
        return settings;
    }

    private String getPropertyOrDefault(String key, String defaultValue) {
        String value = env.getProperty(key);
        return value != null ? value : defaultValue;
    }

    private String maskSensitiveInfo(String value) {
        if (value == null || value.equals("未设置")) {
            return value;
        }
        // 对敏感信息进行掩码处理
        if (value.contains("@")) {
            // 掩码邮箱或数据库URL中的密码
            return value.replaceAll("(?<=:)[^@]+(?=@)", "****");
        }
        return value;
    }

    public Map<String, Object> getSystemInfo() {
        Map<String, Object> info = new HashMap<>();
        
        // JVM信息
        Runtime runtime = Runtime.getRuntime();
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        info.put("JVM版本", System.getProperty("java.version"));
        info.put("JVM供应商", System.getProperty("java.vendor"));
        info.put("JVM内存(已用/最大)", 
            formatSize(memoryBean.getHeapMemoryUsage().getUsed()) + " / " + 
            formatSize(memoryBean.getHeapMemoryUsage().getMax()));
        info.put("JVM可用处理器", runtime.availableProcessors());
        
        // 操作系统信息
        info.put("操作系统", System.getProperty("os.name"));
        info.put("系统架构", System.getProperty("os.arch"));
        info.put("系统版本", System.getProperty("os.version"));
        
        // 磁盘信息
        File root = new File("/");
        info.put("磁盘空间(可用/总共)", 
            formatSize(root.getFreeSpace()) + " / " + 
            formatSize(root.getTotalSpace()));
        
        // 运行时信息
        info.put("启动时间", ManagementFactory.getRuntimeMXBean().getStartTime());
        info.put("运行时长(分钟)", 
            (System.currentTimeMillis() - ManagementFactory.getRuntimeMXBean().getStartTime()) / 60000);
        
        return info;
    }

    private String formatSize(long size) {
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int unitIndex = 0;
        double sizeInUnit = size;
        
        while (sizeInUnit >= 1024 && unitIndex < units.length - 1) {
            sizeInUnit /= 1024;
            unitIndex++;
        }
        
        return String.format("%.2f %s", sizeInUnit, units[unitIndex]);
    }

    public void updateSettings(Map<String, String> settings) {
        // 实现设置更新逻辑
        // 可以保存到数据库或配置文件
    }

    public List<String> getRecentLogs() {
        // 实现日志获取逻辑
        // 可以从日志文件或数据库中读取
        return new ArrayList<>();
    }

    public void clearCache() {
        cacheManager.getCacheNames()
            .forEach(cacheName -> cacheManager.getCache(cacheName).clear());
    }
} 