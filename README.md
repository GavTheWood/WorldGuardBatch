# WorldGuardBatch

Plugin to execute batch commands in WorldGuard

## Commands
Commands are executed in world context. The command is only applied to regions in the current world.

Regions can by identified with an auto increment counter and regex for the name or by owner of the region.

Auto increment works with a placeholder and one or two bounds.  
To select region_1 to region_100 you have to write:  
`/<command> count region_\* 1 100`  
You dont have to type the first number, if it is zero.

Regex is a bit more complex. Google will be your friend.

### Membership
Commands to change the Membership of regions.
Possible scope is Member change, Owner change and both (All)

#### Remove Membership
The prefix for membership removal is `mrem`  
General pattern:  
`/wgb mrem [all/member/owner] [playerToRemove] <count/regex/owner> <args>`

##### General removal
Removes a player from a region. Affects all regions in the current world.  
`/wgb mrem [all/member/owner] [playerToRemove]`

##### Removes with auto increment selection
`/wgb mrem [all/member/owner] [playerToRemove] count [name] [count1] <count2>`

##### Remove a player from a region with regex
Affects all regions, which names matches the regex pattern.  
`/wgb mrem [all/member/owner] [playerToRemove] regex [regex pattern]`

##### Remove a player from regions owned by a specific player
Affects all regions, which are owned by the player. Owned means owner not Member.  
`/wgb mrem [all/member/owner] [playerToRemove] owner [owner]`


#### Add Membership
The prefix for membership assignment is `madd`:  
A player can be added as owner or member of a region.  
General pattern:  
`/wgb madd [member/owner] [playerToAdd] <count/regex/owner> <args>`

##### General membership assignment
Assigns a player as member or owner to all regions.  
`/wgb madd [member/owner] [playerToAdd]`

##### Assign membership with auto increment selection
Assigns a player as member or owner to all regions, which matches the name pattern.  
`/wgb madd [member/owner] [playerToAdd] count [name] [count1] <count2>`

##### Assign membership with regex selection
Assign a player as member or owner to all regions, which matches the regex pattern.  
`/wgb madd [member/owner] [playerToAdd] regex [regex pattern]`

##### Assign membership by owner
Assigns a player as owner or member to all regions owned by another specific player.  
`/wgb madd [member/owner] [playerToAdd] owner [owner]`


#### Transfer Membership
Transfers a membership from a player to another player.  
Owner gives the new Player owner, where the old player was the owner.  
Member give the new player member, where the old player was the member.  
All gives the new player the same membership, which the old player had.  
`/wgb mtrans [all/member/owner] [oldPlayer] [newPlayer]`


### Priority
Changes the priority of a region.   
The region can by selected by parent, counter name and regex.  

Affects all regions.  
`/wgb prio [priority]`

Affects all regions, which are a child of the region.  
`/wgb prio [priority] parent [regionid]`

Affects all regions, which matches the count pattern.  
`/wgb prio [priority] count [name] [count1] <count2>`

Affects all regions, which matches the regex pattern.  
`/wgb prio [priority] regex [regex pattern]`


### Parent/Inheritance
Manage the parent -> child inheritance of regions.

#### Assign parent
Assign a parent to a region. Old parent is removed.  

Affects all regions.  
`/wgb pset [parent]`

Affects all region which matches the regex pattern.  
`/wgb pset [parent] regex [regex pattern]`

Affects all region which matches the count pattern.  
`/wgb pset [parent] count [name] [count1] <count2>`


#### Change parent
Changes the parent of all children to a new parent  
`/wgb pch [oldParent] [newParent]`

#### Remove children from parent
Removes children from a parent region. Region does not have any children after this

Affects all regions, which are a child of this region.  
`/wgb crem [parent]`

Affects all regions, which are a child of this region and matches the count pattern.  
`/wgb crem [parent] count [name] [count1] <count2>`

Affects all regions, which are a child of this region and matches the regex pattern.  
`/wgb crem [parent] regex [regex pattern]`


#### Remove parent from regions
Removes the parent from regions. Region doesn't have a parent after this operation.

Affects all regions matching the count pattern  
`/wgb prem count [name] [count1] <count2>`

Affects all regions matching the regex pattern  
`/wgb prem regex [regex pattern]`

### Flags
Manage flags.  
Flagname and value is the same like in world guard.
#### Set Flags
Affects all regions with matching type pattern.
`/wgb fset regex [regex pattern] [flagname] [flagvalue]`


#### Remove Flags
Affecs all regions with matching name pattern.
`/wgb frem regex [regex pattern] [flagname]`

### Check Commands

#### Count Command
Command to check how many region will be affected by a query.  
Counts all region in world.  
`/wgb check all`

Counts all regions, which have the region as parent.  
`/wgb check children [regionid]`

Counts all regions with matching name.  
`/wgb check regex [regex pattern]`

Counts all regions with matching name.  
`/wgb check count [name] [count1] <count2>`

Counts all regions with matching name.  
`/wgb check owner [name]`

### List Commands
Command to check how many region will be affected by a query.  
Names of affected Regions will be written in the chat.

Lists all region in world.  
`/wgb list all`

Lists all regions, which have the region as parent.  
`/wgb list children [regionid]`

Lists all regions with matching name.  
`/wgb list regex [regex pattern]`

Lists all regions with matching name.  
`/wgb list count [name] [count1] <count2>`

Lists all regions with matching name.  
`/wgb list owner [name]`
