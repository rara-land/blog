package rara.board.controller;

import rara.board.Paging;
import rara.board.Util;
import rara.board.domain.DelStatus;
import rara.board.domain.Member;
import rara.board.domain.Post;
import rara.board.repository.MemberRepository;
import rara.board.repository.PostDto;
import rara.board.repository.PostRepository;
import rara.board.repository.SearchCond;
import rara.board.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@RequestMapping("/board")
@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Value("${custom.path.file-real-upload-path}")
    private String fileRealPath;

    @Value("${custom.path.file-path}")
    private String filePath;

    @GetMapping
    public String list(Model model, @RequestParam(name = "page", defaultValue = "1") int currentPage,
                       @ModelAttribute(name="search") SearchCond search) {
        Paging paging = new Paging(currentPage);
        Long totalCount = postRepository.getTotalCount(search);
        paging.setTotalCount(totalCount);
        paging.setTotalPage();

        List<Post> list = postRepository.findAll(paging, search);

        for (Post post : list) {
            post.setContent(Util.removeTag(post.getContent()));
        }
        model.addAttribute("list", list);
        model.addAttribute("page", paging);
        log.info("paging = {}", paging.toString());
        return "board/list";
    }

    @GetMapping("/form")
    public String addForm(Model model) {
        model.addAttribute("form", new PostDto());
        return "board/form";
    }

    @PostMapping("/form")
    public String add(@Validated @ModelAttribute("form") PostDto postDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "board/form";
        }
        Post savedPost = postService.save(postDto);

        return "redirect:/board/" + savedPost.getId();
    }

    @GetMapping("/{id}")
    public String view(@PathVariable("id") Long id, Model model) {
        Optional<Post> findPost = postRepository.findById(id);

        if (findPost.isEmpty()) {
            model.addAttribute("msg", "?????? ?????? ?????? ????????? ???????????????.");
            return "ex/notValidPost";
        }

        Post post = findPost.get();

        if (post.getDel().equals(DelStatus.Y)) {
            model.addAttribute("msg", "????????? ??????????????????.");
            return "ex/notValidPost";
        }

        Member writer = memberRepository.findById(post.getWriter());
        Optional<Post> previous = postRepository.findPrevious(id);
        Optional<Post> next = postRepository.findNext(id);
        model.addAttribute("data", post);
        model.addAttribute("member", writer);
        model.addAttribute("previous", previous.orElse(null));
        model.addAttribute("next", next.orElse(null));

        return "board/view";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Optional<Post> findPost = postRepository.findById(id);

        if (findPost.isEmpty()) {
            model.addAttribute("msg", "?????? ?????? ?????? ????????? ???????????????.");
            return "ex/notValidPost";
        }

        Post post = findPost.get();

        if (!post.getWriter().equals(Util.getSessionMember().getId())) {
            model.addAttribute("msg", "????????? ?????? ????????? ??? ????????????.");
            return "ex/notValidPost";
        }
        model.addAttribute("form", post);

        return "board/editForm";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, @Validated @ModelAttribute("form") PostDto postDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "board/editForm";
        }
        postService.update(id, postDto.getTitle(), postDto.getContent());

        return "redirect:/board/" + id;
    }

    @ResponseBody
    @PostMapping("/delete/{id}")
    public HashMap<String, Object> delete(@PathVariable("id") Long id) {
        Optional<Post> findPost = postRepository.findById(id);

        HashMap<String, Object> result = new HashMap<>();

        if (findPost.isEmpty()) {
            result.put("result", false);
            result.put("msg", "???????????? ?????? ??????????????????.");

            return result;
        }

        Post post = findPost.get();

        if (post.getDel().equals(DelStatus.Y)) {
            result.put("result", false);
            result.put("msg", "?????? ????????? ??????????????????.");

            return result;
        }

        if (!post.getWriter().equals(Util.getSessionMember().getId())) {

            result.put("result", false);
            result.put("msg", "????????? ?????? ????????? ??? ????????????.");

            return result;
        }

        postService.delete(id);

        result.put("result", true);
        result.put("msg", "?????????????????????.");

        return result;
    }

    @ResponseBody
    @PostMapping("/fileupload")
    public String fileUpload(HttpServletRequest request) throws IOException {
        String sFileInfo = "";
        String fileName = request.getHeader("file-name");
        String fileExt = fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();

        String[] allowFileArr = {"jpg", "png", "bmp", "gif"};
        List<String> allowFile = Arrays.asList(allowFileArr);

        if (allowFile.contains(fileExt)) {
            File file = new File(fileRealPath);

            if (!file.exists()) {
                file.mkdirs();
            }

            String sRealFileNm = "";
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String today= formatter.format(new java.util.Date());
            sRealFileNm = today + fileName.substring(fileName.lastIndexOf("."));
            String rlFileNm = fileRealPath + sRealFileNm;

            InputStream inputStream = request.getInputStream();
            OutputStream outputStream=new FileOutputStream(rlFileNm);
            int numRead;
            byte bytes[] = new byte[Integer.parseInt(request.getHeader("file-size"))];
            while((numRead = inputStream.read(bytes,0,bytes.length)) != -1){
                outputStream.write(bytes,0,numRead);
            }
            if(inputStream != null) {
                inputStream.close();
            }
            outputStream.flush();
            outputStream.close();

            sFileInfo += "&bNewLine=true";
            // img ????????? title ????????? ????????????????????? ?????????????????? ??????
            sFileInfo += "&sFileName="+ fileName;
            sFileInfo += "&sFileURL="+ filePath +sRealFileNm;

            return sFileInfo;

        } else {
            return "NOTALLOW_" + fileName;
        }
    }
}
