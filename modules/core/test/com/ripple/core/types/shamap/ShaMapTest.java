package com.ripple.core.types.shamap;

import com.ripple.config.Config;
import com.ripple.core.serialized.BinarySerializer;
import com.ripple.core.types.hash.Hash256;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import java.util.HashSet;

import static com.ripple.core.types.shamap.ShaMapInnerNode.NodeType.tnTRANSACTION_MD;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class ShaMapTest {
    static {
        Config.initBouncy();
    }
    @Test
    public void testNibblet() throws Exception {
        String ledgerIndex = "D66D0EC951FD5707633BEBE74DB18B6D2DDA6771BA0FBF079AD08BFDE6066056";
        Hash256 index = hash(ledgerIndex);

        for (int i = 0; i < ledgerIndex.length(); i++) {
            int n1 = index.nibblet(i);
            String s = Integer.toHexString(n1).toUpperCase();
            assertEquals(ledgerIndex.substring(i, i + 1), s);
        }
    }

    @Test
    public void testArrayAssumptions() throws Exception {
        String[] arr = new String[16];
        assertEquals(null, arr[0]);
        assertEquals(null, arr[15]);
    }


    @Test
    public void testEndianIssues() throws Exception {
    }

//    @Test
    public void donTestLeafNodeHashing() throws Exception {
        // Note that this is node starting with nibble `2` below
        byte[] tag = Hex.decode("A197ECCF23E55193CBE292F7A373F0DE0F521D4DCAE32484E20EC634C1ACE528");
        final byte[] node = Hex.decode("9E12000822000000002400113FCF201900113F3268400000000000000A73210256C64F0378DCCCB4E0224B36F7ED1E5586455FF105F760245ADB35A8B03A25FD7447304502200A8BED7B8955F45633BA4E9212CE386C397E32ACFF6ECE08EB74B5C86200C606022100EF62131FF50B288244D9AB6B3D18BACD44924D2BAEEF55E1B3232B7E033A27918114E0E893E991B2142E74486F7D3331CF711EA84213C304201C00000001F8E511006125003136FA55610A3178D0A69167DF32E28990FD60D50F5610A5CF5C832CBF0C7FCC0913516B5656091AD066271ED03B106812AD376D48F126803665E3ECBFDBBB7A3FFEB474B2E62400113FCF2D000000456240000000768913E4E1E722000000002400113FD02D000000446240000000768913DA8114E0E893E991B2142E74486F7D3331CF711EA84213E1E1E5110064565943CB2C05B28743AADF0AE47E9C57E9C15BD23284CF6DA9561993D688DA919AE7220000000036561993D688DA919A585943CB2C05B28743AADF0AE47E9C57E9C15BD23284CF6DA9561993D688DA919A01110000000000000000000000004C54430000000000021192D705968936C419CE614BF264B5EEB1CEA47FF403110000000000000000000000004254430000000000041192D705968936C419CE614BF264B5EEB1CEA47FF4E1E1E411006F5678812E6E2AB80D5F291F8033D7BC23F0A6E4EA80C998BFF38E80E2A09D2C4D93E722000000002400113F32250031361633000000000000000034000000000000329255C7D1671589B1B4AB1071E38299B8338632DAD19A7D0F8D28388F40845AF0BCC550105943CB2C05B28743AADF0AE47E9C57E9C15BD23284CF6DA9561993D688DA919A64D4C7A75562493C000000000000000000000000004C5443000000000092D705968936C419CE614BF264B5EEB1CEA47FF465D44AA183A77ECF80000000000000000000000000425443000000000092D705968936C419CE614BF264B5EEB1CEA47FF48114E0E893E991B2142E74486F7D3331CF711EA84213E1E1E511006456F78A0FFA69890F27C2A79C495E1CEB187EE8E677E3FDFA5AD0B8FCFC6E644E38E72200000000310000000000003293320000000000000000582114A41BB356843CE99B2858892C8F1FEF634B09F09AF2EB3E8C9AA7FD0E3A1A8214E0E893E991B2142E74486F7D3331CF711EA84213E1E1F1031000");
        ShaMapLeafNode shaMapLeafNode = new ShaMapLeafNode(new Hash256(tag), 0, tnTRANSACTION_MD, new ShaMapLeafNode.Item() {
            @Override
            public byte[] bytes() {
                return node;
            }
        });
    }


    @Test
    public void testShaMapDeepNodes() {
        Hash256           //0                                                             64

                id1 = hash("0000000000000000000000000000000000000000000000000000000000000000"),
                id2 = hash("1000000000000000000000000000000000000000000000000000000000000000"),
                id3 = hash("2100000000000000000000000000000000000000000000000000000000000000"),
                id4 = hash("2110000000000000000000000000000000000000000000000000000000000000"),
                id5 = hash("2120000000000000000000000000000000000000000000000000000000000000"),
                id6 = hash("3000000000000000000000000000000000000000000000000000000000000000")
                ;

        ensureUnique(id1, id2, id3, id4, id5, id6);

        ShaMapLeafNode.Item 
                i1 = createItem(id1),
                i2 = createItem(id1),
                i3 = createItem(id1),
                i4 = createItem(id1),
                i5 = createItem(id1),
                i6 = createItem(id1);
        
        ShaMap map = new ShaMap();
        map.addLeaf(id1, tnTRANSACTION_MD, i1);
        map.addLeaf(id2, tnTRANSACTION_MD, i2);
        map.addLeaf(id3, tnTRANSACTION_MD, i3);
        map.addLeaf(id4, tnTRANSACTION_MD, i4);
        map.addLeaf(id5, tnTRANSACTION_MD, i5);
        map.addLeaf(id6, tnTRANSACTION_MD, i6);

        // Test leaves
        assertEquals(id1, map.branches[0].id);
        assertEquals(0, map.branches[0].depth);
        assertTrue(map.branches[0] instanceof ShaMapLeafNode);

        assertEquals(id2, map.branches[1].id);
        assertEquals(0, map.branches[1].depth);
        assertTrue(map.branches[1] instanceof ShaMapLeafNode);

        assertEquals(id6, map.branches[3].id);
        assertEquals(0, map.branches[3].depth);
        assertTrue(map.branches[3] instanceof ShaMapLeafNode);

        assertEquals(id2, map.branches[1].id);
        assertEquals(0, map.branches[1].depth);
        assertTrue(map.branches[1] instanceof ShaMapLeafNode);


        assertTrue(map.branches[2] instanceof ShaMapNode);

        assertTrue(     map.branches[2].branches[1] instanceof ShaMapNode);
        assertEquals(2, map.branches[2].branches[1].depth);

        assertTrue(     map.branches[2].branches[1].branches[0] instanceof ShaMapLeafNode);
        assertEquals(2, map.branches[2].branches[1].branches[0].depth);

        assertTrue(map.branches[2].branches[1].branches[1] instanceof ShaMapLeafNode);
        assertTrue(map.branches[2].branches[1].branches[2] instanceof ShaMapLeafNode);


    }

    private void ensureUnique(Hash256... hashes) {
        HashSet<String> s = new HashSet<String>();
        int n=0;

        for (Hash256 hash : hashes) {
            n+=1;
            assertTrue("The " + n + "th hash is a duplicate", s.add(hash.toString()));
        }
    }

    private ShaMapLeafNode.Item createItem(final Hash256 id1) {
        return new ShaMapLeafNode.Item() {
            @Override
            public byte[] bytes() {
                return id1.getBytes();
            }
        };
    }


    @Test
    public void testLedgerHashing() throws Exception {

        Hash256 tx1_hash = hash("232E91912789EA1419679A4AA920C22CFC7C6B601751D6CBE89898C26D7F4394");
        byte[] tx1 = Hex.decode("120007220000000024000195F964400000170A53AC2065D5460561EC9DE000000000000000000000000000494C53000000000092D705968936C419CE614BF264B5EEB1CEA47FF468400000000000000A7321028472865AF4CB32AA285834B57576B7290AA8C31B459047DB27E16F418D6A71667447304502202ABE08D5E78D1E74A4C18F2714F64E87B8BD57444AFA5733109EB3C077077520022100DB335EE97386E4C0591CAC024D50E9230D8F171EEB901B5E5E4BD6D1E0AEF98C811439408A69F0895E62149CFCC006FB89FA7D1E6E5D");
        byte[] tx1Meta = Hex.decode("201C00000000F8E311006F563596CE72C902BAFAAB56CC486ACAF9B4AFC67CF7CADBB81A4AA9CBDC8C5CB1AAE824000195F934000000000000000E501062A3338CAF2E1BEE510FC33DE1863C56948E962CCE173CA55C14BE8A20D7F00064400000170A53AC2065D5460561EC9DE000000000000000000000000000494C53000000000092D705968936C419CE614BF264B5EEB1CEA47FF4811439408A69F0895E62149CFCC006FB89FA7D1E6E5DE1E1E31100645662A3338CAF2E1BEE510FC33DE1863C56948E962CCE173CA55C14BE8A20D7F000E8365C14BE8A20D7F0005862A3338CAF2E1BEE510FC33DE1863C56948E962CCE173CA55C14BE8A20D7F0000311000000000000000000000000494C530000000000041192D705968936C419CE614BF264B5EEB1CEA47FF4E1E1E511006456AB03F8AA02FFA4635E7CE2850416AEC5542910A2B4DBE93C318FEB08375E0DB5E7220000000032000000000000000058801C5AFB5862D4666D0DF8E5BE1385DC9B421ED09A4269542A07BC0267584B64821439408A69F0895E62149CFCC006FB89FA7D1E6E5DE1E1E511006125003136FA55DE15F43F4A73C4F6CB1C334D9E47BDE84467C0902796BB81D4924885D1C11E6D56CF23A37E39A571A0F22EC3E97EB0169936B520C3088963F16C5EE4AC59130B1BE624000195F92D000000086240000018E16CCA08E1E7220000000024000195FA2D000000096240000018E16CC9FE811439408A69F0895E62149CFCC006FB89FA7D1E6E5DE1E1F1031000");

        Hash256 tx2_hash = hash("A197ECCF23E55193CBE292F7A373F0DE0F521D4DCAE32484E20EC634C1ACE528");
        byte[] tx2 = Hex.decode("12000822000000002400113FCF201900113F3268400000000000000A73210256C64F0378DCCCB4E0224B36F7ED1E5586455FF105F760245ADB35A8B03A25FD7447304502200A8BED7B8955F45633BA4E9212CE386C397E32ACFF6ECE08EB74B5C86200C606022100EF62131FF50B288244D9AB6B3D18BACD44924D2BAEEF55E1B3232B7E033A27918114E0E893E991B2142E74486F7D3331CF711EA84213");
        byte[] tx2Meta = Hex.decode("201C00000001F8E511006125003136FA55610A3178D0A69167DF32E28990FD60D50F5610A5CF5C832CBF0C7FCC0913516B5656091AD066271ED03B106812AD376D48F126803665E3ECBFDBBB7A3FFEB474B2E62400113FCF2D000000456240000000768913E4E1E722000000002400113FD02D000000446240000000768913DA8114E0E893E991B2142E74486F7D3331CF711EA84213E1E1E5110064565943CB2C05B28743AADF0AE47E9C57E9C15BD23284CF6DA9561993D688DA919AE7220000000036561993D688DA919A585943CB2C05B28743AADF0AE47E9C57E9C15BD23284CF6DA9561993D688DA919A01110000000000000000000000004C54430000000000021192D705968936C419CE614BF264B5EEB1CEA47FF403110000000000000000000000004254430000000000041192D705968936C419CE614BF264B5EEB1CEA47FF4E1E1E411006F5678812E6E2AB80D5F291F8033D7BC23F0A6E4EA80C998BFF38E80E2A09D2C4D93E722000000002400113F32250031361633000000000000000034000000000000329255C7D1671589B1B4AB1071E38299B8338632DAD19A7D0F8D28388F40845AF0BCC550105943CB2C05B28743AADF0AE47E9C57E9C15BD23284CF6DA9561993D688DA919A64D4C7A75562493C000000000000000000000000004C5443000000000092D705968936C419CE614BF264B5EEB1CEA47FF465D44AA183A77ECF80000000000000000000000000425443000000000092D705968936C419CE614BF264B5EEB1CEA47FF48114E0E893E991B2142E74486F7D3331CF711EA84213E1E1E511006456F78A0FFA69890F27C2A79C495E1CEB187EE8E677E3FDFA5AD0B8FCFC6E644E38E72200000000310000000000003293320000000000000000582114A41BB356843CE99B2858892C8F1FEF634B09F09AF2EB3E8C9AA7FD0E3A1A8214E0E893E991B2142E74486F7D3331CF711EA84213E1E1F1031000");

        ShaMapLeafNode.Item n1 = createItem(tx1, tx1Meta);
        ShaMapLeafNode.Item n2 = createItem(tx2, tx2Meta);

        String node = "C111120007220000000024000195F964400000170A53AC2065D5460561EC9DE000000000000000000000000000494C53000000000092D705968936C419CE614BF264B5EEB1CEA47FF468400000000000000A7321028472865AF4CB32AA285834B57576B7290AA8C31B459047DB27E16F418D6A71667447304502202ABE08D5E78D1E74A4C18F2714F64E87B8BD57444AFA5733109EB3C077077520022100DB335EE97386E4C0591CAC024D50E9230D8F171EEB901B5E5E4BD6D1E0AEF98C811439408A69F0895E62149CFCC006FB89FA7D1E6E5DC26E201C00000000F8E311006F563596CE72C902BAFAAB56CC486ACAF9B4AFC67CF7CADBB81A4AA9CBDC8C5CB1AAE824000195F934000000000000000E501062A3338CAF2E1BEE510FC33DE1863C56948E962CCE173CA55C14BE8A20D7F00064400000170A53AC2065D5460561EC9DE000000000000000000000000000494C53000000000092D705968936C419CE614BF264B5EEB1CEA47FF4811439408A69F0895E62149CFCC006FB89FA7D1E6E5DE1E1E31100645662A3338CAF2E1BEE510FC33DE1863C56948E962CCE173CA55C14BE8A20D7F000E8365C14BE8A20D7F0005862A3338CAF2E1BEE510FC33DE1863C56948E962CCE173CA55C14BE8A20D7F0000311000000000000000000000000494C530000000000041192D705968936C419CE614BF264B5EEB1CEA47FF4E1E1E511006456AB03F8AA02FFA4635E7CE2850416AEC5542910A2B4DBE93C318FEB08375E0DB5E7220000000032000000000000000058801C5AFB5862D4666D0DF8E5BE1385DC9B421ED09A4269542A07BC0267584B64821439408A69F0895E62149CFCC006FB89FA7D1E6E5DE1E1E511006125003136FA55DE15F43F4A73C4F6CB1C334D9E47BDE84467C0902796BB81D4924885D1C11E6D56CF23A37E39A571A0F22EC3E97EB0169936B520C3088963F16C5EE4AC59130B1BE624000195F92D000000086240000018E16CCA08E1E7220000000024000195FA2D000000096240000018E16CC9FE811439408A69F0895E62149CFCC006FB89FA7D1E6E5DE1E1F1031000";
        String node2 = "9E12000822000000002400113FCF201900113F3268400000000000000A73210256C64F0378DCCCB4E0224B36F7ED1E5586455FF105F760245ADB35A8B03A25FD7447304502200A8BED7B8955F45633BA4E9212CE386C397E32ACFF6ECE08EB74B5C86200C606022100EF62131FF50B288244D9AB6B3D18BACD44924D2BAEEF55E1B3232B7E033A27918114E0E893E991B2142E74486F7D3331CF711EA84213C304201C00000001F8E511006125003136FA55610A3178D0A69167DF32E28990FD60D50F5610A5CF5C832CBF0C7FCC0913516B5656091AD066271ED03B106812AD376D48F126803665E3ECBFDBBB7A3FFEB474B2E62400113FCF2D000000456240000000768913E4E1E722000000002400113FD02D000000446240000000768913DA8114E0E893E991B2142E74486F7D3331CF711EA84213E1E1E5110064565943CB2C05B28743AADF0AE47E9C57E9C15BD23284CF6DA9561993D688DA919AE7220000000036561993D688DA919A585943CB2C05B28743AADF0AE47E9C57E9C15BD23284CF6DA9561993D688DA919A01110000000000000000000000004C54430000000000021192D705968936C419CE614BF264B5EEB1CEA47FF403110000000000000000000000004254430000000000041192D705968936C419CE614BF264B5EEB1CEA47FF4E1E1E411006F5678812E6E2AB80D5F291F8033D7BC23F0A6E4EA80C998BFF38E80E2A09D2C4D93E722000000002400113F32250031361633000000000000000034000000000000329255C7D1671589B1B4AB1071E38299B8338632DAD19A7D0F8D28388F40845AF0BCC550105943CB2C05B28743AADF0AE47E9C57E9C15BD23284CF6DA9561993D688DA919A64D4C7A75562493C000000000000000000000000004C5443000000000092D705968936C419CE614BF264B5EEB1CEA47FF465D44AA183A77ECF80000000000000000000000000425443000000000092D705968936C419CE614BF264B5EEB1CEA47FF48114E0E893E991B2142E74486F7D3331CF711EA84213E1E1E511006456F78A0FFA69890F27C2A79C495E1CEB187EE8E677E3FDFA5AD0B8FCFC6E644E38E72200000000310000000000003293320000000000000000582114A41BB356843CE99B2858892C8F1FEF634B09F09AF2EB3E8C9AA7FD0E3A1A8214E0E893E991B2142E74486F7D3331CF711EA84213E1E1F1031000";

        assertEquals(node,  Hex.toHexString(n1.bytes()).toUpperCase());
        assertEquals(node2, Hex.toHexString(n2.bytes()).toUpperCase());

        ShaMap ledger = new ShaMap();
        ledger.addLeaf(tx1_hash, tnTRANSACTION_MD, n1);
        ledger.addLeaf(tx2_hash, tnTRANSACTION_MD, n2);

        String tnh = "7597469704639256442E505C2291DEDF8AEC835C974BC98545D490F462343178";
        Hash256 transaction_hash = hash(tnh);

        assertTrue(transaction_hash.equals(ledger.hash()));
    }

    private ShaMapLeafNode.Item createItem(byte[] tx, byte[] meta) {
        BinarySerializer s = new BinarySerializer();
        s.addLengthEncoded(tx);
        s.addLengthEncoded(meta);
        final byte[] bytes = s.toByteArray();
        return new ShaMapLeafNode.Item() {
            @Override
            public byte[] bytes() {
                return bytes;
            }
        };
    }

    private Hash256 hash(String tnh) {
        return Hash256.translate.fromString(tnh);
    }
}
