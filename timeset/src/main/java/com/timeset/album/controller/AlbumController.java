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
    public List<Album> all(@RequestParam("userId") int userId) {
        System.out.println("查询所有相册");
        return albumService.findAll(userId);
    }

    @RequestMapping("/add")
    public int findByAlbum(@RequestParam("userId") int userId, @RequestParam("theme") String theme, @RequestParam("albumName") String albumName, @RequestParam(value = "albumPic", required = false) String albumPic) {
        System.out.println("增加相册");
        if (userId != 0) {
            Album album = new Album();
            album.setAlbumName(albumName);
            album.setAlbumPic(albumPic);
            album.setUseId(userId);
            album.setTheme(theme);
            int result=albumService.addAlbum(album);
            if(result>0) {
                return album.getId();
            }else {
                return -1;
            }
        }
        return -1;
    }

    @RequestMapping("/delete")
    public int deleteAlbum(@RequestParam("id") int id) {
        System.out.println("删除相册");
        if (id != 0) {
            int result = albumService.deleteAlbum(id);
            if (result != 0) {
                return 0;
            }
        }
        return -1;
    }

    @RequestMapping("/updateName")
    public int updateName(@RequestParam("userId") int userId, @RequestParam("id") int id, @RequestParam("albumName") String albumName) {
        System.out.println("修改相册名");
        if (userId != 0 && id != 0 && albumName != null) {
            int result = albumService.updateNameAlbum(userId, id, albumName);
            if (result != 0) {
                return 0;
            }
        }
        return -1;
    }

    @RequestMapping("/updateTheme")
    public int updateTheme(@RequestParam("userId") int userId, @RequestParam("id") int id, @RequestParam("theme") String theme) {
        System.out.println("修改相册主题");
        if (userId != 0 && id != 0 && theme != null) {
            int result = albumService.updateThemeAlbum(userId, id, theme);
            if (result != 0) {
                return 0;
            }
        }
        return -1;

    }


}
