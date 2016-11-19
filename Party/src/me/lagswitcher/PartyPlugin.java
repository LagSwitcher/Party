package me.lagswitcher;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.event.EventHandler;

@SuppressWarnings("unused")
public class PartyPlugin
  extends Plugin
  implements Listener
{
  public static ProxyServer ps;
  
  public void onEnable()
  {
    ps = ProxyServer.getInstance();
    ProxyServer.getInstance().getPluginManager().registerCommand(this, new PlayerCommande("party"));
    ProxyServer.getInstance().getPluginManager().registerListener(this, this);
  }
  
  @EventHandler
  public void onConnect(ServerConnectedEvent e)
  {
    ProxiedPlayer pp = e.getPlayer();
    if ((Party.hasParty(pp)) && 
      (Party.getParty(pp).isCreator(pp))) {
      for (ProxiedPlayer pp2 : Party.getParty(pp).getPlayers())
      {
        pp2.connect(e.getServer().getInfo());
        pp2.sendMessage(new TextComponent(ChatColor.BLUE + "[Party]: Your party joined a game"));
      }
    }
  }
  
  @EventHandler
  public void onLeave(PlayerDisconnectEvent e)
  {
    ProxiedPlayer pp = e.getPlayer();
    if ((Party.hasParty(pp)) && 
      (Party.getParty(pp).isCreator(pp))) {
      Party.getParty(pp).removeParty();
    }
  }
}
