/*
 * Jobs Plugin for Bukkit
 * Copyright (C) 2011  Zak Ford <zak.j.ford@gmail.com>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package com.zford.jobs.economy.link;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import com.nidefawl.Stats.Stats;
import com.zford.jobs.Jobs;

public class EssentialsLink implements EconomyLink{
	private Jobs plugin;
	public EssentialsLink(Jobs plugin, Essentials essentials){
	    this.plugin = plugin;
	}
	
	@Override
    public void pay(String playername, double amount) {
		try {
			Economy.add(playername, amount);
		} catch (UserDoesNotExistException e) {
			e.printStackTrace();
		} catch (NoLoanPermittedException e) {
			e.printStackTrace();
		}
	}

    @Override
	public void updateStats(String playername) {
        // stats plugin integration
        if(plugin.getJobsConfiguration().getStats() != null &&
                plugin.getJobsConfiguration().getStats().isEnabled()){
            Stats stats = plugin.getJobsConfiguration().getStats();
            double balance;
            try {
                balance = Economy.getMoney(playername);
            } catch(UserDoesNotExistException e) {
                e.printStackTrace();
                return;
            }
            if(balance > stats.get(playername, "job", "money")){
                stats.setStat(playername, "job", "money", (int) balance);
                stats.saveAll();
            }
        }
    }

}
