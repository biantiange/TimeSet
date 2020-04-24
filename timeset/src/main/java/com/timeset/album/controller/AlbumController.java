package com.timeset.album.controller;

import com.timeset.album.entity.Album;
import com.timeset.album.service.AlbumServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName PhotoController
 * @Description
 * @Author lz
 * @Date 2020-04-23 18:06
 */
@RestController
@RequestMapping("/album")
public class AlbumController {
    @Resource
    private AlbumServiceImpl albumService;

    @RequestMapping("/all")
    public List<Album> all(){
        System.out.println("查询所有相册");
        return albumService.findAll();
    }

    @RequestMapping("/add")
    public void findByAlbum(@RequestParam("userId") int userId,@RequestParam("theme")String theme,@RequestParam("albumName")String albumName){
        System.out.println("增加相册");
        if(userId!=0 && theme!=null && !theme.equals("") && albumName!=null && !albumName.equals("")) {
            albumService.addAlbum(userId,theme,albumName);
        }
    }

    @RequestMapping("/delete")
    public void deleteAlbum(@RequestParam("userId") int userId,@RequestParam("id")int id){
        System.out.println("删除相册");
        if(userId!=0 && id!=0){
            albumService.deleteAlbum(userId,id);
        }
    }

    @RequestMapping("/updateName")
    public void updateName(@RequestParam("userId") int userId,@RequestParam("id")int id,@RequestParam("albumName") String albumName){
        System.out.println("修改相册名");
        if(userId!=0 && id!=0 && albumName!=null){
           albumService.updateNameAlbum(userId,id,albumName);
        }
    }

    @RequestMapping("/updateTheme")
    public void updateTheme(@RequestParam("userId") int userId,@RequestParam("id")int id,@RequestParam("theme") String theme){
        System.out.println("修改相册主题");
        if(userId!=0 && id!=0 && theme!=null){
            albumService.updateThemeAlbum(userId,id,theme);
        }

    }


}
