package com.bookmark.selectview;

public class MainClass {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		
		ViewClass vc = new ViewClass();
		
		
		
	

        
       
		
        String[] title = {
                "██████╗  ██████╗  ██████╗ ██╗  ██╗███╗   ███╗ █████╗ ██████╗ ██╗  ██╗",
                "██╔══██╗██╔═══██╗██╔═══██╗██║ ██╔╝████╗ ████║██╔══██╗██╔══██╗██║ ██╔╝",
                "██████╔╝██║   ██║██║   ██║█████╔╝ ██╔████╔██║███████║██████╔╝█████╔╝ ",
                "██╔══██╗██║   ██║██║   ██║██╔═██╗ ██║╚██╔╝██║██╔══██║██╔══██╗██╔═██╗ ",
                "██████╔╝╚██████╔╝╚██████╔╝██║  ██╗██║ ╚═╝ ██║██║  ██║██║  ██║██║  ██╗",
                "╚═════╝  ╚═════╝  ╚═════╝ ╚═╝  ╚═╝╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝"
            };

            for (String line : title) {
                System.out.println(line);
                Thread.sleep(150); // 100ms 간격으로 한 줄씩 출력
            }
            
            System.out.println("\n\n"); 
        	
            System.out.println("                .-~~~~~~~~~-._       _.-~~~~~~~~~-.");
            System.out.println("            __.'              ~.   .~              `.__");
            System.out.println("          .'//                  \\./                  \\\\`.");
            System.out.println("        .'//                     |                     \\\\`.");
            System.out.println("      .'// .-~\"\"\"\"\"\"\"~~~~-._     |     _,-~~~~\"\"\"\"\"\"\"~-. \\\\`.");
            System.out.println("    .'//.-\"                 `-.  |  .-'                 \"-.\\\\`.");
            System.out.println("  .'//______.============-..   \\ | /   ..-============.______\\\\`.");
            System.out.println(".'______________________________\\|/______________________________`.");

        
            
            System.out.println("\n\n"); 
        
		//showView method
		vc.showView();
	}

}
