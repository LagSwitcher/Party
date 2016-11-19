package me.lagswitcher;

import java.util.HashMap;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PlayerCommande
  extends Command
{
  private HashMap<String, Party> playerParty = new HashMap<String, Party>();
  
  public PlayerCommande(String name)
  {
    super(name);
  }
  
  public void execute(CommandSender sender, String[] args)
  {
    if ((sender instanceof ProxiedPlayer))
    {
      ProxiedPlayer pp = (ProxiedPlayer)sender;
      if (args.length == 0)
      {
        pp.sendMessage(new TextComponent(ChatColor.AQUA + "<-----------------[Party]-------------------->"));
        pp.sendMessage(new TextComponent(ChatColor.GREEN + "/party create - Create your party"));
        pp.sendMessage(new TextComponent(ChatColor.GREEN + "/party invite <name> - Invite someone to your party"));
        pp.sendMessage(new TextComponent(ChatColor.GREEN + "/party have - Check if you are in a party"));
        pp.sendMessage(new TextComponent(ChatColor.GREEN + "/party join - Join the party last "));
        pp.sendMessage(new TextComponent(ChatColor.GREEN + "/party disband - Remove your party"));
        pp.sendMessage(new TextComponent(ChatColor.GREEN + "/party kick <name> - Kick a player from your party"));
        pp.sendMessage(new TextComponent(ChatColor.GREEN + "/party leave - Leave the current party"));
        pp.sendMessage(new TextComponent(ChatColor.GREEN + "/party chat <message> - Party Chat"));
        pp.sendMessage(new TextComponent(ChatColor.GREEN + "Only the creator can invite and remove"));
        pp.sendMessage(new TextComponent(ChatColor.GREEN + "the people of the party"));
        pp.sendMessage(new TextComponent(ChatColor.AQUA + "<-----------------[Party]-------------------->"));
      }
      if (args.length == 1)
      {
        if (args[0].equalsIgnoreCase("create")) {
          if (Party.hasParty(pp))
          {
            pp.sendMessage(new TextComponent(ChatColor.AQUA + "[Party]: A party has already been created! "));
          }
          else
          {
            new Party(pp);
            pp.sendMessage(new TextComponent(ChatColor.AQUA + "[Party]: Your party has been created!"));

          }
        }
        if (args[0].equalsIgnoreCase("leave")) {
          if (Party.hasParty(pp))
          {
            Party.getParty(pp).removePlayer(pp);
            pp.sendMessage(new TextComponent(ChatColor.AQUA + "[Party]: You left your party!"));
          }
          else
          {
            pp.sendMessage(new TextComponent(ChatColor.AQUA + "[Party]: You do not have to party"));
          }
        }
        if (args[0].equalsIgnoreCase("disband")) {
          if (Party.hasParty(pp))
          {
            if (Party.getParty(pp).isCreator(pp))
            {
              Party.getParty(pp).removeParty();
              pp.sendMessage(new TextComponent(ChatColor.AQUA + "[Party]: Party removed!"));
            }
            else
            {
              pp.sendMessage(new TextComponent(ChatColor.AQUA + "[Party]: You are not the creator of this party!"));
            }
          }
          else {
            pp.sendMessage(new TextComponent(ChatColor.AQUA + "[Party]: You do not have to party"));
          }
        }
        if (args[0].equalsIgnoreCase("have")) {
          if (Party.hasParty(pp)) {
            pp.sendMessage(new TextComponent(ChatColor.AQUA + "[Party]: You are in the party of " + Party.getParty(pp).getCreateur().getDisplayName()));
          } else {
            pp.sendMessage(new TextComponent(ChatColor.AQUA + "[Party]: You do not have to party"));
          }
        }
        if (args[0].equalsIgnoreCase("join")) {
          if (this.playerParty.containsKey(pp.getName()))
          {
            if (Party.hasParty(pp))
            {
              pp.sendMessage(new TextComponent("&3[Party]: &7Have a party!"));
            }
            else
            {
              ((Party)this.playerParty.get(pp.getName())).addPlayer(pp);
              pp.sendMessage(new TextComponent(ChatColor.AQUA + "[Party]: You joined the party of " + Party.getParty(pp).getName()));
              Party.getParty(pp).getCreateur().sendMessage(new TextComponent(ChatColor.AQUA + "[Party]: " + pp.getName() + "joined your party!"));
            }
          }
          else {
            pp.sendMessage(new TextComponent(ChatColor.AQUA + "[Party]: You have not received any invitation"));
          }
        }
      }
      if (args.length == 2)
      {
        if (args[0].equalsIgnoreCase("invite")) {
          if (Party.hasParty(pp))
          {
            if (Party.getParty(pp).isCreator(pp)) {
              try
              {
                ProxiedPlayer pp2 = PartyPlugin.ps.getPlayer(args[1]);
                if (!Party.hasParty(pp2))
                {
                  this.playerParty.put(pp2.getName(), Party.getParty(pp));
                  pp.sendMessage(new TextComponent(ChatColor.AQUA + "[Party]: Player guest"));
                  pp2.sendMessage(new TextComponent(ChatColor.AQUA + "[Party]: You have been invited to join the party of " + Party.getParty(pp).getName()));
                  pp2.sendMessage(new TextComponent(ChatColor.AQUA + "[Party]: Type /party join to join"));
                }
                else
                {
                  pp.sendMessage(new TextComponent(ChatColor.AQUA + "[Party]: Players already in a party. Unable to invite"));
                }
              }
              catch (Error e)
              {
                pp.sendMessage(new TextComponent(ChatColor.AQUA + "[Party]: Error."));
              }
            } else {
              pp.sendMessage(new TextComponent(ChatColor.AQUA + "[Party]: You are not the creator of this party!"));
            }
          }
          else {
            pp.sendMessage(new TextComponent(ChatColor.AQUA + "[Party]: You do not have to party"));
          }
        }
        if (args[0].equalsIgnoreCase("kick"))
        {
          ProxiedPlayer pp2 = PartyPlugin.ps.getPlayer(args[1]);
          if (Party.hasParty(pp))
          {
            if (Party.getParty(pp).isCreator(pp)) {
              try
              {
                Party.getParty(pp).removePlayer(PartyPlugin.ps.getPlayer(args[1]));
                pp.sendMessage(new TextComponent(ChatColor.AQUA + "[Party]: Player kicked!"));
                pp2.sendMessage(new TextComponent(ChatColor.AQUA + "[Party]: You are kicked of your party!"));
              }
              catch (Error e)
              {
                pp.sendMessage(new TextComponent(ChatColor.AQUA + "[Party]: Error"));
              }
            } else {
              pp.sendMessage(new TextComponent(ChatColor.AQUA + "[Party]: You are not the creator of this party"));
            }
          }
          else {
            pp.sendMessage(new TextComponent(ChatColor.AQUA + "[Party]: You do not have to party"));
          }
        }
        if ((args.length >= 2) && 
          (args[0].equalsIgnoreCase("chat"))) {
          if (Party.hasParty(pp))
          {
            String message = "";
            for (int i = 1; i < args.length; i++) {
              message = message + " " + args[i];
            }
            message.trim();
            String messagefinal = message.replace("&", "&");
            for (ProxiedPlayer pp2 : Party.getParty(pp).getPlayers()) {
              pp2.sendMessage(new TextComponent(ChatColor.AQUA + "[Party][" + pp.getName() + "] > " + messagefinal));
            }
          }
          else
          {
            pp.sendMessage(new TextComponent(ChatColor.AQUA + "[Party]: You do not have to party"));
          }
        }
      }
    }
  }
}