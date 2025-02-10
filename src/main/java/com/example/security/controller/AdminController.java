package com.example.security.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.security.entity.Role;
import com.example.security.entity.User;
import com.example.security.service.RoleService;
import com.example.security.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("roles", roleService.findAllRoles());
        return "admin/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("allRoles", roleService.findAllRoles());
        return "admin/user-edit";
    }

    @PostMapping("/users/update")
    public String updateUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            userService.updateUser(user);
            redirectAttributes.addFlashAttribute("success", "用户更新成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "更新失败: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "用户删除成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "删除失败: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/users/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.findAllRoles());
        return "admin/user-add";
    }

    @PostMapping("/users/add")
    public String addUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            userService.registerUser(user);
            redirectAttributes.addFlashAttribute("success", "用户添加成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "添加失败: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/roles")
    public String listRoles(Model model) {
        model.addAttribute("roles", roleService.findAllRoles());
        return "admin/roles";
    }

    @GetMapping("/roles/add")
    public String addRoleForm(Model model) {
        model.addAttribute("role", new Role());
        return "admin/role-add";
    }

    @PostMapping("/roles/add")
    public String addRole(@ModelAttribute Role role, RedirectAttributes redirectAttributes) {
        try {
            roleService.createRole(role);
            redirectAttributes.addFlashAttribute("success", "角色添加成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "添加失败: " + e.getMessage());
        }
        return "redirect:/admin/roles";
    }

    @GetMapping("/roles/edit/{id}")
    public String editRoleForm(@PathVariable Long id, Model model) {
        model.addAttribute("role", roleService.findById(id));
        return "admin/role-edit";
    }

    @PostMapping("/roles/update")
    public String updateRole(@ModelAttribute Role role, RedirectAttributes redirectAttributes) {
        try {
            roleService.updateRole(role);
            redirectAttributes.addFlashAttribute("success", "角色更新成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "更新失败: " + e.getMessage());
        }
        return "redirect:/admin/roles";
    }

    @PostMapping("/roles/delete/{id}")
    public String deleteRole(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            roleService.deleteRole(id);
            redirectAttributes.addFlashAttribute("success", "角色删除成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "删除失败: " + e.getMessage());
        }
        return "redirect:/admin/roles";
    }

    @GetMapping("/users/{id}/roles")
    public String editUserRoles(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("allRoles", roleService.findAllRoles());
        return "admin/user-roles";
    }

    @PostMapping("/users/{id}/roles")
    public String updateUserRoles(@PathVariable Long id,
                                @RequestParam(required = false) List<Long> roleIds,
                                RedirectAttributes redirectAttributes) {
        try {
            userService.updateUserRoles(id, roleIds != null ? roleIds : new ArrayList<>());
            redirectAttributes.addFlashAttribute("success", 
                "用户角色更新成功。如果角色发生变化，用户需要重新登录才能生效。");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "更新失败: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }
} 