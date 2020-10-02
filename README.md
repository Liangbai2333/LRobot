# [LRobot][0]
[0]:https://www.github.com/Liangbai2333/LRobot
`LRobot`是运行在`Mirai`框架的`动态插件加载框架`，基于`ASM`+`URLClassLoader`与一套强大的注解系统(开发中)实现，现已支持Java, Kotlin语言，`LRobot-Native`初步支持原生语言(C++, C)及易语言。
## 声明
* 本项目仍在开发阶段，所有API都有可能在并不知情的情况下删除，所有插件都并不稳定。
* 本项目暂未向外公布，预计更新到LRobot-2.0 Alpha时向外公布，同时也将稳定。
* 现版本(1.8 Alpha)，仍是半成品阶段，现有代码即将重置。
* `LRobot` 采用 `AGPL 3.0` 协议开源。
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
@SubscribeEvent
class Example : JavaPlugin() {
  override fun onEnable() {
    Bot.getGroup(123456).sendMessage(
    "插件启动"
    )
  }

  @JvmStatic
  @EventHandler
  fun onMsg(event: GroupMessageEvent)
  {
    event.group.sendMessage(
      "${LR.at(event.sender)}
      [复读] ${event.message}"
    )
  }
}
```

## 鸣谢: Bukkit, Intellij IDEA
