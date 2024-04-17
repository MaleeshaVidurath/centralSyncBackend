package CentralSync.demo.service;

import CentralSync.demo.exception.AdjustmentNotFoundException;
import CentralSync.demo.model.Adjustment;
import CentralSync.demo.model.Status;
import CentralSync.demo.repository.AdjustmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class AdjustmentServiceImpl implements AdjustmentService {

    @Autowired //ingect the repository
    private AdjustmentRepository adjustmentRepository;

//    private final String FOLDER_PATH = "https://drive.google.com/drive/folders/164D0U68Y1_EUTDx_YIk_LjSp3gqq5ujw";

    @Override
    public Adjustment saveAdjustment(Adjustment adjustment){
        return adjustmentRepository.save(adjustment);
    }

    @Override
    public List<Adjustment> getAllAdjustments() {
        return adjustmentRepository.findAll();
    }

    @Override
    public Adjustment getAdjustmentById(Long adjId) {
        return adjustmentRepository.findById(adjId)
                .orElseThrow(()-> new AdjustmentNotFoundException(adjId));
    }

    @Override
    public Adjustment updateAdjustmentById(Adjustment newAdjustment, Long adjId){
        return adjustmentRepository.findById(adjId)
                .map(adjustment -> {
                    adjustment.setStatus(newAdjustment.getStatus());
                    adjustment.setReason(newAdjustment.getReason());
                    adjustment.setDate(newAdjustment.getDate());
                    adjustment.setDescription(newAdjustment.getDescription());
                    adjustment.setNewQuantity(newAdjustment.getNewQuantity());

                    return adjustmentRepository.save(adjustment);
                }).orElseThrow(()->new AdjustmentNotFoundException(adjId));
    }
    @Override
    public String deleteAdjustmentById(Long adjId){
        if(!adjustmentRepository.existsById(adjId)){
            throw new AdjustmentNotFoundException(adjId);
        }
        adjustmentRepository.deleteById(adjId);
        return  "Adjustment with id "+adjId+" has been deleted successfully.";
    }

    //file upload
//    public String uploadFileToFileSystem(MultipartFile file)throws IOException{
//        String filePath = FOLDER_PATH+file.getOriginalFilename();
//
//        Adjustment adjustment = adjustmentRepository.save(Adjustment.builder()
//                        .name(file.getOriginalFilename())
//                .filePath(filePath).build());
//
//        file.transferTo(new File(filePath));
//
//        if(adjustment != null){
//            return "file uploaded successfully : "+ filePath;
//        }
//        return null;
//    }

    //download file
//    public byte[] downloadFileFromFileSystem(String fileName)throws IOException{
//        Optional<Adjustment> adjustment = adjustmentRepository.findByName(fileName);
//        String filePath = adjustment.get().getFilePath();
//        byte[] files = Files.readAllBytes(new File(filePath).toPath());
//        return files;
//    }

    //new
    private final String FOLDER_PATH = "/Users/Ashen/Desktop/centralSyncFile/";

    public void uploadFile(Long adjId, MultipartFile file) throws IOException {
        Optional<Adjustment> optionalAdjustment = adjustmentRepository.findById(adjId);
        if (optionalAdjustment.isPresent()) {
            Adjustment adjustment = optionalAdjustment.get();
            adjustment.setFilePath(FOLDER_PATH + file.getOriginalFilename());
            adjustment.setFileContent(file.getBytes());
            adjustmentRepository.save(adjustment);
            saveFileToDisk(file);
        } else {
            // Handle adjustment not found
        }
    }

    private void saveFileToDisk(MultipartFile file) throws IOException {
        Path filePath = Paths.get(FOLDER_PATH + file.getOriginalFilename());
        Files.write(filePath, file.getBytes());
    }

    public byte[] downloadFile(Long adjId) {
        Optional<Adjustment> optionalAdjustment = adjustmentRepository.findById(adjId);
        if (optionalAdjustment.isPresent()) {
            Adjustment adjustment = optionalAdjustment.get();
            return adjustment.getFileContent();
        } else {
            // Handle adjustment not found
            return null;
        }
    }

    @Override
    public Adjustment updateAdjStatusAccept(Long adjId) {
        return adjustmentRepository.findById(adjId)
                .map(adjustment -> {
                    adjustment.setStatus(Status.ACCEPTED);
                    return adjustmentRepository.save(adjustment);
                })
                .orElseThrow(()->new AdjustmentNotFoundException(adjId));
    }

    @Override
    public Adjustment updateAdjStatusReject(Long adjId) {
        return adjustmentRepository.findById(adjId)
                .map(adjustment -> {
                    adjustment.setStatus(Status.REJECTED);
                    return adjustmentRepository.save(adjustment);
                })
                .orElseThrow(()->new AdjustmentNotFoundException(adjId));
    }
}


