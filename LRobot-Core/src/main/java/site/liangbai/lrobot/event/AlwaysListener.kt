package site.liangbai.lrobot.event

import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent
import net.mamoe.mirai.event.events.MemberJoinRequestEvent
import net.mamoe.mirai.event.events.NewFriendRequestEvent
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.message.FriendMessageEvent
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.TempMessageEvent
import site.liangbai.lrobot.LRobot
import site.liangbai.lrobot.contact.Friend
import site.liangbai.lrobot.contact.Group
import site.liangbai.lrobot.contact.Sender
import site.liangbai.lrobot.message.Chain
import kotlin.reflect.jvm.jvmName

fun start(bot: Bot) {
    bot.subscribeAlways<Event> {
        val name = if(EventUtils.registeredListener.containsKey(this::class.simpleName)) this::class.simpleName else this::class.jvmName
        when(this) {
            is NewFriendRequestEvent -> {
                val evt = callEvent(name, this)
                if(evt is site.liangbai.lrobot.event.friend.NewFriendRequestEvent) {
                    if(!evt.isCancelled) this.accept()
                }
            }
            is MemberJoinRequestEvent -> {
                val evt = callEvent(name, this)
                if(evt is site.liangbai.lrobot.event.group.member.MemberJoinRequestEvent) {
                    if(!evt.isCancelled) this.accept()
                }
            }
            is BotInvitedJoinGroupRequestEvent -> {
                val evt = callEvent(name, this)
                if(evt is site.liangbai.lrobot.event.bot.group.BotInvitedJoinGroupRequestEvent) {
                    if(!evt.isCancelled) this.accept()
                }
            }
            is GroupMessageEvent -> {
                val chain = Chain(message)
                if(chain.messageIgnoreCode.startsWith("/")) {
                    LRobot.getCommandMap().dispatch(Group(group.id), sender.id, chain.messageIgnoreCode.substring(1))
                }
                callEvent(name, this)
            }
            is FriendMessageEvent -> {
                val chain = Chain(message)
                if(chain.messageIgnoreCode.startsWith("/")) {
                    LRobot.getCommandMap().dispatch(Friend(friend.id), sender.id, chain.messageIgnoreCode.substring(1))
                }
                callEvent(name, this)
            }
            is TempMessageEvent -> {
                val chain = Chain(message)
                if(chain.messageIgnoreCode.startsWith("/")) {
                    LRobot.getCommandMap().dispatch(Sender(sender), sender.id, chain.messageIgnoreCode.substring(1))
                }
                callEvent(name, this)
            }
            else -> {
                callEvent(name, this)
            }
        }
    }
}

private fun callEvent(name: String?, event: Event): site.liangbai.lrobot.event.Event? {
    if(EventUtils.registeredListener.containsKey(name)) {
        val constructor = EventUtils.registeredListener[name]?.getConstructor(event::class.java)
                ?: throw IllegalArgumentException("Can't find constructor: $name")
        val evt = constructor.newInstance(event)
        EventUtils.callEvent(evt)
        return evt
    }
    return null
}