# [LRobot][0]
[0]:https://www.github.com/Liangbai2333/LRobot
`LRobot`是运行在`Mirai`框架的`动态插件加载框架`，基于`ASM`+`URLClassLoader`与一套注解系统(开发中)实现，现已支持Java, Kotlin语言。
## 声明
* 本项目仍在开发阶段，所有API都有可能在并不知情的情况下删除。
* `LRobot` 采用 `AGPL 3.0` 协议开源。
## 未来计划
* 支持 C++、C、EPL等语言。
## 开源许可证
```
Copyright (C) 2020 Liangbai Technologies and contributors.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
```
## 极简示例
Java: 
``` 
@Plugin(name = "Example", version = "1.0", author = "Liangbai")
@SubscribeEvent
public class Example extends JavaPlugin {
  @Override
  public void onEnable() {
    Bot.getGroup(123456).sendMessage(
      "插件启动"
    );
  }
  
  @EventHandler
  public static void onMsg(GroupMessageEvent event) {
   event.getGroup().sendMessage(
     LR.at(event.getSender())
     + "[复读] "
     + event.getMessage()
   );
  }
}
```
Kotlin: 
```
@Plugin(name = "Example", version = "1.0", author = "Liangbai")
class Example : JavaPlugin() {
  override fun onEnable() {
    Bot.getGroup(123456).sendMessage(
    "插件启动"
    EventUtils.registerEvents(this, object : Listener {
      @EventHandler
      fun onMsg(event: GroupMessageEvent)
      {
        event.group.sendMessage(
          "${LR.at(event.sender)}
           [复读] ${event.message}"
        )
      }
    )
  }
}
```
## 指令系统
目前有两个指令注册方式:
- 在实现CommandExecutor的public类上添加注解@CommandVisitor(commandName)，插件加载时会自动识别并加载它.
- 使用LRobot#getCommandMap#register方法.

## 监听系统
监听方法实例:
@EventHandler
public (static) void onEvent(GroupMessageEvent event)
其中EventHandler不可缺失.
目前有两个监听注册方式:
- 在public类上添加注解@SubscribeEvent，并将所有监听方法标识为static
- 使用EventUtils#registerEvents方法.

## 鸣谢: Bukkit, Intellij IDEA
