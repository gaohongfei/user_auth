package com.example.security.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")  // 明确指定表名
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column
    private String description;

    @JsonIgnore // 防止序列化时的循环引用
    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)  // 改为EAGER加载
    private Set<User> users = new HashSet<>();

    // 重写toString方法避免循环引用和懒加载问题
    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    // 添加无参构造函数
    public Role() {
        this.users = new HashSet<>();
    }

    // 添加equals和hashCode方法
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return id != null && id.equals(role.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
} 