# StSModdingToolCardImagesCreator

This is the tool I created to help create images for [The Blackbeard Mod](https://github.com/JohnnyBazooka89/StSModTheBlackbeard). I hope it will be useful for other Modders.

This tool takes images with the ratio 25:19 and cuts them to fit into Slay the Spire cards. 

Specifically when processing images with size 500x380 or larger, it creates 6 versions of every image: Attack portrait, Attack, Skill portrait, Skill, Power portrait and Power. When processing images with size between 500x380 and 250x190, it creates 3 versions of every image: Attack, Skill and Power.

Usually it is enough to provide just the image with size 500x380, but in case of vector graphics or texts the automatic 50% scaling can look pretty bad, then it is advised to also provide 250x190 version of the image and use this one for small version of the card instead. You can compare ChaoticDefend.png and ChaoticDefendSmall.png to see the difference.

This screenshot shows example files generated by the tool:
![](CreatedImages.png)

To use the tool, simply download the newest [release](https://github.com/JohnnyBazooka89/StSModdingToolCardImagesCreator/releases), then copy images you want to process into the folder "cards" and open "run.bat". The tool creates a new folder named "images" and inside it there will be 3 folders: "Attacks", "Powers" and "Skills". Copy the files you need into your mod. That's all!  
